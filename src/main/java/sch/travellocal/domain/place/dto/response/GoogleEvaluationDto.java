package sch.travellocal.domain.place.dto.response;

import lombok.*;

/**
 * google map의 place에 대한 평가 정보
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class GoogleEvaluationDto {

    private int reviewCount;
    private double rating;
    private String googleMapsUrl;
}
