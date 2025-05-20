package sch.travellocal.domain.tourprogram.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sch.travellocal.common.response.SuccessResponse;
import sch.travellocal.domain.tourprogram.dto.request.SaveTourProgramRequestDto;
import sch.travellocal.domain.tourprogram.dto.response.TourProgramDetailResponseDto;
import sch.travellocal.domain.tourprogram.service.TourProgramService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tour-program")
@Tag(name = "TourProgram", description = "투어 프로그램 API")
public class TourProgramController {

    private final TourProgramService tourProgramService;

    /**
     * 게시물 저장 API
     * 작성한 상세 게시물 정보 반환 (게시물 저장 이후 보여지는 화면이 본인이 작성한 게시물 상세 페이지)
     */
    @PostMapping
    @Operation(
            summary = "투어 프로그램 등록",
            description = "투어 프로그램을 새로 등록합니다. 저장 후에는 상세 페이지 정보를 응답으로 제공합니다."
    )
    public ResponseEntity<SuccessResponse<TourProgramDetailResponseDto>> saveTourProgram(@RequestBody SaveTourProgramRequestDto requestDto) {

        return ResponseEntity.ok(SuccessResponse.ok(tourProgramService.saveTourProgram(requestDto)));
    }

    /**
     * 게시물 조회 API
     * 게시물 상세 정보 + 작성자 정보 + 위시리스트/리뷰 개수 응답으로 반환
     */
    @GetMapping("/{tourProgramId}")
    @Operation(
            summary = "특정 투어 프로그램 상세조회",
            description = "특정 투어 프로그램 id를 통해 상세 페이지 정보를 응답으로 제공합니다."
    )
    public ResponseEntity<SuccessResponse<TourProgramDetailResponseDto>> getDetailTourProgram(@PathVariable Long tourProgramId) {

        TourProgramDetailResponseDto responseDto = tourProgramService.getDetailTourProgram(tourProgramId);
        return ResponseEntity.ok(SuccessResponse.ok(responseDto));
    }

    /**
     * 게시물 수정 API
     * 수정한 상세 게시물 정보 반환 (게시물 수정 이후 보여지는 화면이 본인이 수정한 게시물 상세 페이지)
     */

    @PutMapping("/{tourProgramId}")
    @Operation(
            summary = "특정 투어 프로그램 수정",
            description = "특정 투어 프로그램의 내용을 수정합니다. 수정된 상세 페이지 정보를 응답으로 제공합니다."
    )
    public ResponseEntity<SuccessResponse<TourProgramDetailResponseDto>> updateTourProgram(@PathVariable Long tourProgramId, @RequestBody SaveTourProgramRequestDto requestDto) {

        return ResponseEntity.ok(SuccessResponse.ok(tourProgramService.updateTourProgram(tourProgramId, requestDto)));
    }

    /**
     * 게시물 삭제 API
     */
    @Operation(
            summary = "특정 투어 프로그램 삭제",
            description = "특정 투어 프로그램 id를 통해 삭제합니다."
    )
    @DeleteMapping("/{tourProgramId}")
    public ResponseEntity<SuccessResponse<String>> deleteTourProgram(@PathVariable Long tourProgramId) {

        return ResponseEntity.ok(SuccessResponse.ok(tourProgramService.deleteTourProgram(tourProgramId)));
    }
}
