package sch.travellocal.domain.place.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sch.travellocal.common.response.SuccessResponse;
import sch.travellocal.domain.place.dto.request.GetPlaceDetailRequestDto;
import sch.travellocal.domain.place.dto.response.PlaceDetailResponse;
import sch.travellocal.domain.place.service.PlaceService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/place")
@Tag(name = "Place", description = "장소 API")
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping
    @Operation(
            summary = "특정 장소 상세조회",
            description = "특정 장소에 대한 place_id를 통해 상세 페이지 정보를 응답으로 제공합니다."
    )
    public ResponseEntity<SuccessResponse<PlaceDetailResponse>> getAsyncPlaceDetail(
            @Valid @ModelAttribute GetPlaceDetailRequestDto requestDto) {

        PlaceDetailResponse placeDetailResponse = placeService.getPlaceDetail(requestDto.getPlaceName(), requestDto.getGooglePlaceId(), requestDto.getLanguage());
        return ResponseEntity.ok(SuccessResponse.ok(placeDetailResponse));
    }
}
