package sch.travellocal.domain.tourprogram.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ReviewDto {

    private Long authorId;
    private String authorName;

    private Long reviewId;
    private float rating;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
