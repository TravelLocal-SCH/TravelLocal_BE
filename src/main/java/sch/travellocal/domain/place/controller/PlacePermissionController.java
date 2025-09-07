package sch.travellocal.domain.place.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sch.travellocal.auth.oauth.CustomOAuth2User;
import sch.travellocal.common.response.SuccessResponse;
import sch.travellocal.domain.place.dto.request.VerifyLocationRequestDto;
import sch.travellocal.domain.place.service.PlacePermissionService;

@RestController
@RequestMapping("/api/place/permission")
@RequiredArgsConstructor
@Tag(name = "Place_Permission", description = "장소에 대한 권한 API")
public class PlacePermissionController {

    private final PlacePermissionService placePermissionService;

    /**
     * 리뷰 저장 API
     * 리뷰 저장 이후 리뷰 객체를 반환해서 상세 리뷰 페이지를 보여줘야 할지, 응답 완료 메시지만 보내야 할지 고민
     */
    @Operation(
            summary = "장소 권한 API",
            description = "사용자의 위치 정보(위/경도) & Google Place_Id를 통해 서버에서 두 지점 간의 거리 차를 계산하여 임계치보다 가깝다면 해당 장소에 위치한 것으로 판단하고 권한을 제공한다."
    )
    @PostMapping
    public ResponseEntity<SuccessResponse<String>> verifyLocationAndGrantPermission(
            @Valid @RequestBody VerifyLocationRequestDto requestDto,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {

        return ResponseEntity.ok(SuccessResponse.ok(placePermissionService.verifyAndGrantPermission(requestDto, customOAuth2User.getUsername())));
    }
}
