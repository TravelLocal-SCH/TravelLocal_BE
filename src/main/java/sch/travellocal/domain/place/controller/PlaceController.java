package sch.travellocal.domain.place.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
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

    // 프론트로부터 google place id를 받아서 장소 정보를 가져오고, 함께 해당 place에 대해 자체적으로 관리하는 리뷰, 평점, 리뷰수를 반환하는 API
    // 확장성을 위해 place에 대한 공급자명(google), place id를 받도록 설계
    private final PlaceService placeService;

    @GetMapping
    @Operation(
            summary = "특정 장소 상세조회",
            description = "특정 장소에 대한 place_id를 통해 상세 페이지 정보를 응답으로 제공합니다."
    )
    public Mono<ResponseEntity<SuccessResponse<PlaceDetailResponse>>> getPlaceDetail(
            @Valid @ModelAttribute GetPlaceDetailRequestDto requestDto) {

        return placeService.getPlaceDetail(requestDto.getPlaceName(), requestDto.getGooglePlaceId(), requestDto.getLanguage())
                .map(SuccessResponse::ok)
                .map(ResponseEntity::ok);
    }
}
