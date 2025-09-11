package sch.travellocal.domain.place.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;


@Getter
@Setter
public class GetPlaceDetailRequestDto {

    @NotBlank(message = "placeName must not be blank")
    private String placeName;

    @NotBlank(message = "placeId must not be blank")
    private String googlePlaceId;

    @Pattern(
            regexp = "kor|eng|jpn",
            message = "language must be one of: kor, eng, jpn"
    )
    private String language = "kor";
}
