package sch.travellocal.upload.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sch.travellocal.common.response.SuccessResponse;
import sch.travellocal.upload.request.S3UploadRequest;
import sch.travellocal.upload.response.S3FileUploadResponse;
import sch.travellocal.upload.service.S3Service;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/upload")
@Tag(name = "S3", description = "이미지 API")
public class S3Controller {

    private final S3Service s3Service;

    @GetMapping
    @Operation(
            summary = "Presigned URL 요청 API",
            description = "프론트는 이미지를 업로드하기 위해 업로드할 filename, contentType을 보내서 Presigned URL, download URL을 받는다.\n" +
                    "Presigned URL를 사용해 이미지를 업로드하면 이미지업로드가 끝난다.\n" +
                    "download URL는 업로드된 이미지를 볼 수 있는 URL로 이것을 게시물 업로드할 때 image_url에 넣어서 보내면 게시물 업로드가 완료된다.\n" +
                    "Presigned URL: 이미지 업로드를 위한 URL, download URL: 저장한 이미지에 대한 URL"
    )
    public ResponseEntity<SuccessResponse<S3FileUploadResponse>> generatePresignedUrl(@ModelAttribute S3UploadRequest request) {

        S3FileUploadResponse response = s3Service.generatePresignedUrl(request.getFileName(), request.getContentType());

        return ResponseEntity.ok(SuccessResponse.ok("Presigned URL 생성 성공", response));
    }

    @DeleteMapping
    @Operation(
            summary = "이미지 삭제",
            description = "downloadUrl첨부하여 해당 파일을 삭제한다."
    )
    public ResponseEntity<SuccessResponse<String>> deleteFile(@RequestParam String downloadUrl) {

        s3Service.deleteFile(downloadUrl);

        return ResponseEntity.ok(SuccessResponse.ok("파일 삭제 성공"));
    }
}