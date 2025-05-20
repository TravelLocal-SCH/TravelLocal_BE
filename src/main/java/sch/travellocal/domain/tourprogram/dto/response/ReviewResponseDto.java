package sch.travellocal.domain.tourprogram.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ReviewResponseDto {

    // 특정 TourProgram에 대한 Review에는 작성자의 정보가 포함되어 있어야 함
    // 유저 정보
    private Long userId;
    private String name;

    // 리뷰 정보
    private float rating;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> imagesUrls;
}
