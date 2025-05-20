package sch.travellocal.domain.tourprogram.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetWishlistsRequestDto {

    private int page = 0;
    private int size = 10;
    @Pattern(regexp = "addedAsc|addedDesc|priceAsc|priceDesc", message = "sortOption must be one of: addedAsc, addedDesc, priceAsc, priceDesc")
    private String sortOption = "addedDesc";
}
