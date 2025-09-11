package sch.travellocal.domain.place.dto.response;

import lombok.*;

/**
 * tour api의 place에 대한 상세정보
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourApiPlaceInfoDto {

    private String name;
    private String address;
    private String description;
    private String imageUrl;
    private String link;
}
