package sch.travellocal.domain.place.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * front에 장소 상세 페이지에서 보여줄 정보 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceDetailResponse {

    // 장소 상세정보
    private TourApiPlaceInfoDto tourApiPlaceInfo;
    private GooglePlaceInfoDto googlePlaceInfo;

    // 각 플랫폼마다의 장소에 대한 평가 정보
    private GoogleEvaluationDto googleEvaluation;
    // kakao map
    // naver map

    // Travellocal의 자체적 장소에 대한 평가 정보
    private TravelLocalEvaluationDto travelLocalEvaluation;
}
