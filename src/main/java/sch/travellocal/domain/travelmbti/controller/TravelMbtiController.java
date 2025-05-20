package sch.travellocal.domain.travelmbti.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sch.travellocal.common.response.SuccessResponse;
import sch.travellocal.domain.travelmbti.dto.ResponseDetailMbtiDTO;
import sch.travellocal.domain.travelmbti.dto.ResponseSimpleMbtiDTO;
import sch.travellocal.domain.travelmbti.dto.base.TravelMbtiDTO;
import sch.travellocal.domain.travelmbti.service.TravelMbtiService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mbti")
@Tag(name = "Travel_Mbti", description = "여행 성향 API")
public class TravelMbtiController {

    private final TravelMbtiService travelMbtiService;

    /**
     * 여행 성향 저장 API
     */
    @PostMapping
    @Operation(
            summary = "여행 성향 등록",
            description = "등록 이후 성공했다면 동일한 화면에서 등록 성공 메시지를 띄어주거나, 메인페이지로 이동하는 두가지 중 하나의 화면을 제공한다. (결정하면 말해줘)"
    )
    public ResponseEntity<SuccessResponse<String>> saveTravelMbti(@RequestBody TravelMbtiDTO travelMbtiDTO) {

        return ResponseEntity.ok(SuccessResponse.ok(travelMbtiService.saveTravelMbti(travelMbtiDTO)));
    }

    /**
     * 모든 여행 성향 조회 API
     */
    @GetMapping("/all-mbti")
    @Operation(
            summary = "유저의 여행 성향 리스트 조회",
            description = "요청에서 JWT를 통해 요청자의 모든 여행 성향 리스트를 응답으로 제공합니다."
    )
    public ResponseEntity<SuccessResponse<List<ResponseSimpleMbtiDTO>>> getAllTravelMbti() {

        List<ResponseSimpleMbtiDTO> mbtis = travelMbtiService.getAllTravelMbti();
        return ResponseEntity.ok(SuccessResponse.ok(mbtis));
    }

    /**
     * 특정 여행 성향 조회 API
     */
    @GetMapping("/detail-mbti")
    @Operation(
            summary = "유저의 여행 성향 상세조회",
            description = "요청에서 JWT를 통해 요청자가 선택한 여행 성향의 상세 정보를 응답으로 제공합니다."
    )
    public ResponseEntity<SuccessResponse<ResponseDetailMbtiDTO>> getDetailTravelMbti(@RequestParam("mbtiId") Long mbtiId, @RequestParam("mbti") String mbti) {

        ResponseDetailMbtiDTO detailMbti = travelMbtiService.getDetailTravelMbti(mbtiId, mbti);
        return ResponseEntity.ok(SuccessResponse.ok(detailMbti));
    }

    /**
     * 여행 성향 삭제 API
     */
    @DeleteMapping
    @Operation(
            summary = "여행 성향 삭제",
            description = "요청자가 선택한 여행 성향을 삭제한다."
    )
    public ResponseEntity<SuccessResponse<String>> deleteTravelMbti(@RequestParam("mbtiId") Long mbtiId, @RequestParam("mbti") String mbti) {

        return ResponseEntity.ok(SuccessResponse.ok(travelMbtiService.deleteTravelMbti(mbtiId, mbti)));
    }
}
