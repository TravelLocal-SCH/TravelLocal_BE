package sch.travellocal.domain.place.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sch.travellocal.common.response.SuccessResponse;
import sch.travellocal.domain.place.dto.request.GetPlaceReviewsRequestDto;
import sch.travellocal.domain.place.dto.request.PlaceReviewDto;
import sch.travellocal.domain.place.dto.request.SavePlaceReviewRequestDto;
import sch.travellocal.domain.place.dto.response.PlaceReviewResponseDto;
import sch.travellocal.domain.place.service.PlaceReviewService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/place/review")
@Tag(name = "Place_Review", description = "(장소권한 추가로 인한 수정: 응답에 verificationBadge가 추가됨) 장소에 대한 리뷰 API")
public class PlaceReviewController {

    private final PlaceReviewService placeReviewService;

    /**
     * 리뷰 저장 API
     * 리뷰 저장 이후 리뷰 객체를 반환해서 상세 리뷰 페이지를 보여줘야 할지, 응답 완료 메시지만 보내야 할지 고민
     */
    @PostMapping
    @Operation(
            summary = "리뷰 등록",
            description = """
                    Place에 대한 리뷰 작성 권한(GPS로 해당 위치에 접근했었던게 확인됨)을 검증한 후 리뷰 정보를 저장합니다.<br>
                    사용자에겐 리뷰 화면이 보여야하니 다시 리뷰 리스트 조회에 대한 요청을 해줘야 합니다.
                    """
    )
    public ResponseEntity<SuccessResponse<List<PlaceReviewResponseDto>>> saveReview(@Valid @RequestBody SavePlaceReviewRequestDto request) {

        List<PlaceReviewResponseDto> reviews = placeReviewService.saveReview(request);
        return ResponseEntity.ok(SuccessResponse.ok(reviews));
    }

    /**
     * 특정 게시물의 모든 리뷰 조회 API (게시물의 리뷰 보기 버튼 클릭 시)
     */
    @GetMapping("/{googlePlaceId}")
    @Operation(
            summary = "Place에 리뷰 리스트 조회",
            description = "Place에 대한 리뷰 리스트를 응답으로 제공합니다."
    )
    public ResponseEntity<SuccessResponse<List<PlaceReviewResponseDto>>> getReviewsByPlace(
            @PathVariable String googlePlaceId,
            @Valid @ModelAttribute GetPlaceReviewsRequestDto request
    ) {

        List<PlaceReviewResponseDto> reviews = placeReviewService.getReviewsByPlace(googlePlaceId, request);
        return ResponseEntity.ok(SuccessResponse.ok(reviews));
    }

    /**
     * 리뷰 삭제 API
     */
    @DeleteMapping
    @Operation(
            summary = "리뷰 삭제",
            description = "리뷰를 삭제합니다. 사용자에겐 리뷰 화면이 보여야하니 다시 리뷰 리스트에 대한 요청을 해줘야 합니다."
    )
    public ResponseEntity<SuccessResponse<String>> deleteReview(@RequestParam String googlePlaceId, @RequestParam Long reviewId) {

        return ResponseEntity.ok(SuccessResponse.ok(placeReviewService.deleteReview(googlePlaceId, reviewId)));
    }

    /**
     * 리뷰 수정 API
     * 추가해야 함
     */


    /**
     * 유저가(요청자가) 작성한 모든 리뷰 조회 API (본인의 리뷰 관리 버튼 클릭 시)
     * jwt를 통해 user정보를 가져와 요청자의 모든 리뷰 조회
     */
    @GetMapping("/my")
    public ResponseEntity<SuccessResponse<List<PlaceReviewDto>>> getMyReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "latest") String sortOption
    ) {
        List<PlaceReviewDto> reviews = placeReviewService.getMyReviews(
                page,
                size,
                sortOption
        );

        return ResponseEntity.ok(SuccessResponse.ok(reviews));
    }
}
