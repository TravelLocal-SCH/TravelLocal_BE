package sch.travellocal.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sch.travellocal.auth.oauth.CustomOAuth2User;
import sch.travellocal.common.response.SuccessResponse;
import sch.travellocal.domain.user.dto.request.UserInfoRequestDto;
import sch.travellocal.domain.user.dto.response.UserInfoResponseDto;
import sch.travellocal.domain.user.service.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "(추가) 유저 API")
public class UserController {

    private final UserService userService;

    // google, kakao 소셜로그인 시에 scope에 따라서 받지 못한 사용자 정보 추가로 받는 API
    // 사용자 정보 조회 API
    @Operation(
            summary = "유저 상세정보 조회",
            description = "unique한 username을 통해 사용자의 정보 조회"
    )
    @GetMapping
    public ResponseEntity<SuccessResponse<UserInfoResponseDto>> getUserInfo(@AuthenticationPrincipal CustomOAuth2User customOAuth2User)
    {

        UserInfoResponseDto userInfoResponseDto = userService.getUserInfo(customOAuth2User);
        return ResponseEntity.ok(SuccessResponse.ok(userInfoResponseDto));
    }


    // 사용자 정보 수정 API
    @Operation(
            summary = "유저 상세정보 수정",
            description = "수정하기 또는 공급자의 scope 설정으로 인해 받지 못한 사용자 정보를 추가로 받을 때 사용하는 API " +
                    "(시나리오: " +
                    "1. 본인 정보 확인 -> 정보 수정 -> 수정하기 클릭 시 해당 API 호출" +
                    "2. 소셜로그인(kakao, google은 제공받지 못한 사용자 정보 존재) 이후 추가로 사용자 정보 제공 받을 때 해당 API 호출" +
                    ")"
    )
    @PutMapping("{username}")
    public ResponseEntity<SuccessResponse<String>> updateUserInfo(
            @Valid @RequestBody UserInfoRequestDto userInfoRequestDto,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User)
    {
        System.out.println("customOAuth2User: " + customOAuth2User.toString());

        String response = userService.updateUserInfo(userInfoRequestDto, customOAuth2User);
        return ResponseEntity.ok(SuccessResponse.ok(response));
    }
}
