package sch.travellocal.domain.tourprogram.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class UserReviewResponseDto {

    // 나의 리뷰를 확인할 때는 해당 리뷰가 어떤 게시물에 대한 리뷰인지 확인해야 함
    // tourProgram 정보
    private Long tourProgramId;
    private String title;

    // 리뷰 정보
    private float rating;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> imagesUrls;
}
