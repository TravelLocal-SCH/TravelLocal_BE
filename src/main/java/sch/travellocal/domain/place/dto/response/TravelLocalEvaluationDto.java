package sch.travellocal.domain.place.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 앱의 자체적 place에 대한 평가 정보
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelLocalEvaluationDto {

    private int reviewCount;
    private double rating;
    // 최신 리뷰 5개만 제공, 이후에 리뷰 더보기 클릭 시 추가 리뷰 가져오기
    private List<PlaceReviewResponseDto> reviews;
}
