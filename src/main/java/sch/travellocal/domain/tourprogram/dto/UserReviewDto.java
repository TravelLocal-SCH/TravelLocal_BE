package sch.travellocal.domain.tourprogram.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class UserReviewDto {

    private Long tourProgramId;
    private String title;

    private Long reviewId;
    private float rating;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
