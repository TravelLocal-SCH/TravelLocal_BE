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
    // firstImage 아니고 firstimage 임!!!! tour api랑 변수명 같아야 하는데 놓치지마!!!!!
    private String firstimage;
    private String homepage;

    public TourApiPlaceInfoDto toDto() {
        return TourApiPlaceInfoDto.builder()
                .name(title)
                .address(addr1)
                .description(overview)
                .imageUrl(firstimage)
                .link(homepage)
                .build();
    }
}
