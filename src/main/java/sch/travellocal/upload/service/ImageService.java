package sch.travellocal.upload.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sch.travellocal.common.exception.custom.ApiException;
import sch.travellocal.common.exception.error.ErrorCode;
import sch.travellocal.upload.entity.Image;
import sch.travellocal.upload.enums.ImageTargetType;
import sch.travellocal.upload.repository.ImageRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    public void saveImages(ImageTargetType imageTargetType, Long targetId, List<String> imageUrls) {

        try {
            // 리뷰 업로드 이미지들 순서 보장하여 저장
            if (imageUrls != null && !imageUrls.isEmpty()) {
                List<Image> images = new ArrayList<>();
                int seq = 0;
                for (String url : imageUrls) {
                    images.add(Image.builder()
                            .imageUrl(url)
                            .sequence(seq++)
                            .targetType(imageTargetType)
                            .targetId(targetId)
                            .build());
                }
                imageRepository.saveAll(images);
            }
        }
        catch (Exception e) {
            throw new ApiException(ErrorCode.INVALID_OPERATION, "이미지 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
