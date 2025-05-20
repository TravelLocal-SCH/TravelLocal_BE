package sch.travellocal.upload.service;

import jakarta.validation.ValidationException;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sch.travellocal.upload.enums.MediaType;
import sch.travellocal.upload.response.S3FileUploadResponse;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${aws.bucket}")
    private String bucket;

    @Value("${aws.url}")
    private String downloadUrl;

    public S3FileUploadResponse generatePresignedUrl(String fileName, String contentType) {
        verifyMimeType(contentType, fileName);

        String feature = contentType.split("/")[0].toLowerCase().equals("image") ? "image" : "video";
        String uniqueFileName = feature + "/" + UUID.randomUUID() + "-" + fileName;

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(uniqueFileName)
                    .contentType(contentType)
                    .build();

            Duration expiration = Duration.ofMinutes(5);

            PresignedPutObjectRequest presignedPutObjectRequest = s3Presigner.presignPutObject(
                    presignRequest -> presignRequest.putObjectRequest(putObjectRequest)
                            .signatureDuration(expiration)
            );

            return S3FileUploadResponse.createResponse(
                    presignedPutObjectRequest.url().toString(),
                    downloadUrl + "/" + uniqueFileName
            );

        } catch (Exception e) {
            log.error("S3 Presigned URL 생성 실패: {}", e.getMessage(), e);
            throw new RuntimeException("S3 URL 생성 실패");
        }
    }

    public boolean deleteFile(String downloadUrl) {
        if (downloadUrl == null || downloadUrl.isBlank()) {
            log.warn("파일 삭제 요청이 들어왔지만, fileName이 null 또는 빈 문자열입니다.");
            return false;
        }

        // 경로에 삭제하고 싶은 파일의 여부를 파악하고 존재하지 않는 경로(잘못된 요청)인지 확인 후 응답으로 알려줄 수도 있지만 어차피 삭제하려는 의도의 요청이였다면
        // 존재하지 않으면 안 지워도 되니까 문제 없으며 추가적인 연산을 수행할 큰 가치를 가지지 않는다고 생각하여 성공으로 처리되도록 함
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(downloadUrl)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            return true;
        } catch (S3Exception e) {
            log.error("S3 파일 삭제 실패: {}", e.awsErrorDetails().errorMessage(), e);
            throw new RuntimeException("S3 URL 삭제 실패");
        }
    }

    // download Url로부터 key 추출 후 삭제
    public void deleteFileByFileName(String downloadUrl) {

        String FileKey = downloadUrl.replace(this.downloadUrl + "/", "");
        System.out.println("FileKey: " + FileKey);

        // s3에 저장되어 있는 게시물 이미지 삭제
        deleteFile(FileKey);
    }

    private void verifyMimeType(String contentType, String fileName) {
        String fileExtension = FilenameUtils.getExtension(fileName).toLowerCase();

        if (!isValidMimeType(contentType, fileExtension)) {
            String errorMsg = String.format("유효하지 않은 MIME 타입 또는 확장자입니다. contentType=%s, extension=%s", contentType, fileExtension);
            log.warn(errorMsg);
            throw new ValidationException(errorMsg);
        }
    }

    private boolean isValidMimeType(String contentType, String fileExtension) {
        for (MediaType type : MediaType.values()) {
            if (type.getValue().equals(contentType)) {
                String mimeExtension = contentType.split("/")[1].toLowerCase();
                if (mimeExtension.equals(fileExtension)) {
                    return true;
                }
            }
        }
        return false;
    }
}
