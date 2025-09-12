package sch.travellocal.domain.place.dto.wrapper;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import sch.travellocal.domain.place.dto.TourApiSearchItemDto;

import java.util.List;

/**
 * tour api의 /searchKeyword2 API 호출 결과를 래핑
 *
 * tour api가 반환하는 JSON의 구조
 * {
 *   "response": {
 *     "items": [ ... ]
 *   }
 * }
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TourApiSearchResponseWrapper {
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
        @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
        @JsonProperty("item")
        private List<TourApiSearchItemDto> item;
    }

//    @Getter
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public static class TourApiSearchItemDto {
//
//        private String contentid;
//    }

    public String getFirstContentId() {
        if (response != null
                && response.getBody() != null
                && response.getBody().getItems() != null
                && response.getBody().getItems().getItem() != null
                && !response.getBody().getItems().getItem().isEmpty()) {
            return response.getBody().getItems().getItem().get(0).getContentid();
        }
        System.out.println("getFirstContentId: " + response.getBody().getItems().getItem().get(0).getContentid());
        return null;
    }
}