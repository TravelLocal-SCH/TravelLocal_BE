package sch.travellocal.domain.tourprogram.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sch.travellocal.common.response.SuccessResponse;
import sch.travellocal.domain.tourprogram.dto.request.GetReviewsRequestDto;
import sch.travellocal.domain.tourprogram.dto.request.SaveReviewRequestDto;
import sch.travellocal.domain.tourprogram.dto.response.ReviewResponseDto;
import sch.travellocal.domain.tourprogram.dto.response.UserReviewResponseDto;
import sch.travellocal.domain.tourprogram.service.ReviewService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
@Tag(name = "TourProgram_Review", description = "투어 프로그램에 대한 리뷰 API")
public class ReviewController {

    private final ReviewService tpReviewService;

    /**
     * 리뷰 저장 API
     * 리뷰 저장 이후 리뷰 객체를 반환해서 상세 리뷰 페이지를 보여줘야 할지, 응답 완료 메시지만 보내야 할지 고민
     */
    @PostMapping
    @Operation(
            summary = "리뷰 등록",
            description = "리뷰 정보를 저장합니다. 사용자에겐 리뷰 화면이 보여야하니 다시 리뷰 리스트 조회에 대한 요청을 해줘야 합니다."
    )
    public ResponseEntity<SuccessResponse<String>> saveReview(@Valid @RequestBody SaveReviewRequestDto request) {

        return ResponseEntity.ok(SuccessResponse.ok(tpReviewService.saveReview(request)));
    }

    /**
     * 특정 게시물의 모든 리뷰 조회 API (게시물의 리뷰 보기 버튼 클릭 시)
     */
    @GetMapping("/{tourProgramId}")
    @Operation(
            summary = "투어 프로그램에 리뷰 리스트 조회",
            description = "특정 투어 프로그램에 대한 리뷰 리스트를 응답으로 제공합니다."
    )
    public ResponseEntity<SuccessResponse<List<ReviewResponseDto>>> getReviewsByTourProgram(
            @PathVariable Long tourProgramId,
            @Valid @ModelAttribute GetReviewsRequestDto request
            ) {

        List<ReviewResponseDto> reviews = tpReviewService.getReviewsByTourProgram(tourProgramId, request.getPage(), request.getSize(), request.getSortOption());
        return ResponseEntity.ok(SuccessResponse.ok(reviews));
    }

    /**
     * 유저가(요청자가) 작성한 모든 리뷰 조회 API (본인의 리뷰 관리 버튼 클릭 시)
     * jwt를 통해 user정보를 가져와 요청자의 모든 리뷰 조회
     */
    @GetMapping
    @Operation(
            summary = "User에 대한 리뷰 리스트 조회",
            description = "요청에서 JWT를 통해 요청자가 작성한 리뷰 리스트를 응답으로 제공합니다."
    )
    public ResponseEntity<SuccessResponse<List<UserReviewResponseDto>>> getReviewsByUser(@Valid @ModelAttribute GetReviewsRequestDto request) {

        List<UserReviewResponseDto> reviews = tpReviewService.getReviewsByUser(request.getPage(), request.getSize(), request.getSortOption());
        return ResponseEntity.ok(SuccessResponse.ok(reviews));
    }

    /**
     * 리뷰 삭제 API
     */
    @DeleteMapping("{tourProgramId}")
    @Operation(
            summary = "리뷰 삭제",
            description = "리뷰를 삭제합니다. 사용자에겐 리뷰 화면이 보여야하니 다시 리뷰 리스트에 대한 요청을 해줘야 합니다.\n" +
                    "(유저에 대한 리뷰를 삭제할 때도 특정 프로그램 정보가 있으므로 그것을 통해 동일하게 해당 API 사용하면 됨)"
    )
    public ResponseEntity<SuccessResponse<String>> deleteReview(@PathVariable Long tourProgramId) {

        return ResponseEntity.ok(SuccessResponse.ok(tpReviewService.deleteReview(tourProgramId)));
    }
}
