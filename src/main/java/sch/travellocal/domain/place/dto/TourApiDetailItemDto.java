package sch.travellocal.domain.place.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import sch.travellocal.domain.place.dto.response.TourApiPlaceInfoDto;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TourApiDetailItemDto {

    private String title;
    private String addr1;
    private String overview;
    // 존재 안할 때도 있음
    private String firstImage;
    private String homepage;

    public TourApiPlaceInfoDto toDto() {
        return TourApiPlaceInfoDto.builder()
                .name(title)
                .address(addr1)
                .description(overview)
                .imageUrl(firstImage)
                .link(homepage)
                .build();
    }
}
