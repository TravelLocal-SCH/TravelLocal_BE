package sch.travellocal.domain.point.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sch.travellocal.auth.oauth.CustomOAuth2User;
import sch.travellocal.common.response.SuccessResponse;
import sch.travellocal.domain.point.dto.request.PointTransactionRequestDto;
import sch.travellocal.domain.point.dto.response.PointBalanceResponseDto;
import sch.travellocal.domain.point.service.PointService;

@RestController
@RequestMapping("/api/points")
@RequiredArgsConstructor
@Tag(name = "Points", description = "(추가) 포인트 API")
public class PointController {

    private final PointService pointService;

    /**
     * 포인트 적립 API
     *
     */
    @Operation(
            summary = "포인트 적립 API",
            description = """
                    EARN(적립)만 사용가능하며 응답으론 사용자의 잔여 포인트 반환<br>
                    actionType 항목: [EARN, USE, EXPIRE, ROLLBACK]<br>
                    actionSubject 항목: [MISSION, PROMOTION, CONTENT, ADMIN]
                    """
    )
    @PostMapping("/earn")
    public ResponseEntity<SuccessResponse<PointBalanceResponseDto>> earnPoints(@Valid @RequestBody PointTransactionRequestDto pointTransactionRequestDto) {

        PointBalanceResponseDto pointBalanceResponseDto = pointService.earnPoints(pointTransactionRequestDto);
        return ResponseEntity.ok(SuccessResponse.ok(pointBalanceResponseDto));
    }

    /**
     * 포인트 사용 API
     *
     */
    @Operation(
            summary = "포인트 사용 API",
            description = """
                    USE(적립)만 사용가능하며 응답으론 사용자의 잔여 포인트 반환<br>
                    actionType 항목: [EARN, USE, EXPIRE, ROLLBACK]<br>
                    actionSubject 항목: [MISSION, PROMOTION, CONTENT, ADMIN]
                    """
    )
    @PostMapping("/use")
    public ResponseEntity<SuccessResponse<PointBalanceResponseDto>> usePoints(@Valid @RequestBody PointTransactionRequestDto pointTransactionRequestDto) {

        PointBalanceResponseDto pointBalanceResponseDto = pointService.usePoints(pointTransactionRequestDto);
        return ResponseEntity.ok(SuccessResponse.ok(pointBalanceResponseDto));
    }

    /**
     * 잔여 포인트 API
     *
     */
    @Operation(
            summary = "잔여 포인트 API",
            description = "사용자의 잔여 포인트 반환"
    )
    @GetMapping("/balance")
    public ResponseEntity<SuccessResponse<PointBalanceResponseDto>> getPointBalance(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

        PointBalanceResponseDto responseDto = pointService.getPointBalance(customOAuth2User.getUsername());
        return ResponseEntity.ok(SuccessResponse.ok(responseDto));
    }

    // 포인트 내역 조회 api

    // 포인트 조회 조건으로(적립, 사용, 시간순) 내역 페이징 조회 api
}
