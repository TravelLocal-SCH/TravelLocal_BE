package sch.travellocal.domain.place.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * google map의 place에 대한 평가 정보
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoogleEvaluationDto {

    private int reviewCount;
    private double rating;
    private String googleMapsUrl;
}
