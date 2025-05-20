package sch.travellocal.domain.tourprogram.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetReviewsRequestDto {

    private int page = 0;
    private int size = 10;
    @Pattern(regexp = "addedDesc|ratingAsc|ratingDesc", message = "sortOption must be one of: addedDesc, ratingAsc, ratingDesc")
    private String sortOption = "addedDesc";
}
