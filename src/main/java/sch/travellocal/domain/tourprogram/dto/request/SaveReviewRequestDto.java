package sch.travellocal.domain.tourprogram.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import java.util.List;

@Getter
public class SaveReviewRequestDto {

    @NotBlank
    @Pattern(regexp = "0.0|0.5|1.0|1.5|2.0|2.5|3.0|3.5|4.0|4.5|5.0", message = "rating must be one of: 0.0, 0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0")
    private String rating;

    private String content;

    @NotNull
    private Long tourProgramId;

    private List<String> imageUrls; // S3 URL 리스트
}
