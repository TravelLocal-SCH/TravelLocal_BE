package sch.travellocal.domain.place.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * tour api의 place에 대한 상세정보
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GooglePlaceInfoDto {

    private String openingHours;
    private String phone;
}
