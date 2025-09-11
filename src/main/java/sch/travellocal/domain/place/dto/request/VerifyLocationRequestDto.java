package sch.travellocal.domain.place.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VerifyLocationRequestDto {

    @NotBlank
    private String googlePlaceId;
    @NotNull
    private Double userLat;
    @NotNull
    private Double userLon;
    @NotNull
    private Double radiusInKm; // 사용자가 검증을 요청할 반경 (예: 1, 2, 3 km)
}