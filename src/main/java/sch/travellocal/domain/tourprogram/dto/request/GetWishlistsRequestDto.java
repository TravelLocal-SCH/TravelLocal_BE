package sch.travellocal.domain.tourprogram.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetWishlistsRequestDto {

    @Min(0)
    private int page = 0;

    @Min(1)
    private int size = 10;

    @Pattern(
            regexp = "addedAsc|addedDesc|priceAsc|priceDesc",
            message = "sortOption must be one of: addedAsc, addedDesc, priceAsc, priceDesc"
    )
    private String sortOption = "addedDesc";
}
