package sch.travellocal.domain.place.dto.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import sch.travellocal.domain.place.dto.TourApiDetailItemDto;
import sch.travellocal.domain.place.dto.response.TourApiPlaceInfoDto;

import java.util.List;

/**
 * tour api의 /detailCommon2 API 호출 결과를 래핑
 *
 * Tour API가 반환하는 JSON의 구조:
 * {
 *   "response": {
 *     "body": {
 *       "items": {
 *         "item": { ... }
 *       }
 *     }
 *   }
 * }
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TourApiDetailWrapper {

    private Response response;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
        private Body body;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body {
        private Items items;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Items {
        // 단일 객체도 리스트로 매핑되도록 처리
        //@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
        private List<TourApiDetailItemDto> item;
    }

    /**
     * 첫 번째 item을 DTO로 변환
     * 찾고자 하는 장소는 0번째에 존재
     */
    public TourApiPlaceInfoDto toDto() {
        if (response != null &&
                response.getBody() != null &&
                response.getBody().getItems() != null &&
                response.getBody().getItems().getItem() != null &&
                !response.getBody().getItems().getItem().isEmpty()) {

            return response.getBody().getItems().getItem().get(0).toDto();
        }
        return null;
    }
}
