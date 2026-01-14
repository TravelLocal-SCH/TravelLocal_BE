package sch.travellocal.domain.place.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceReviewResponseDto {

    // 특정 TourProgram에 대한 Review에는 작성자의 정보가 포함되어 있어야 함
    // 유저 정보
    private AuthorDto author;
    @JsonProperty("isAuthor")
    private boolean isAuthor;

    // 리뷰 정보
    private Long reviewId;
    private float rating;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> imagesUrls;
    private boolean verificationBadge;
}
