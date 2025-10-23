package sch.travellocal.domain.tourprogram.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class GetMyTourProgramRequestDto {

    @Min(0)
    private int page = 0;

    @Min(1)
    private int size = 10;

    @Pattern(
            regexp = "latest|oldest",
            message = "sortOption must be one of: latest, oldest"
    )
    private String sortOption = "latest";
}