package sch.travellocal.domain.tourprogram.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetTourProgramListRequestDto {

    @NotEmpty
    private List<String> hashtags;

    @NotEmpty
    private List<String> regions;

    @Min(0)
    private int page = 0;

    @Min(1)
    private int size = 10;

    @Pattern(
            regexp = "addedAsc|addedDesc|priceAsc|priceDesc|reviewDesc|wishlistDesc",
            message = "sortOption must be one of: addedAsc, addedDesc, priceAsc, priceDesc, reviewDesc, wishlistDesc"
    )
    private String sortOption = "addedDesc";

}
