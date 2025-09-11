package sch.travellocal.domain.place.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

/**
 * tour api의 /searchKeyword2 API에서 필요한 데이터만 바인딩
 *
 * tour api의 /detailCommon2 API 호출을 위해 contentid 필요
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TourApiSearchItemDto {

    // contentid 임!! contentId로 해놓으면 tour api 파싱할 때 변수명 달라서 문제 생김... 제발!!!!
    private String contentid;
}
