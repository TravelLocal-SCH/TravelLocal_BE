package sch.travellocal.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sch.travellocal.auth.service.SocialLoginService;

@RestController
@RequiredArgsConstructor
@Tag(name = "SocialLogin", description = "소셜로그인 API")
public class SocialLoginController {

    private final SocialLoginService socialLoginService;

    /**
     * token 재발급 API
     */
    @PostMapping("/reissue")
    @Operation(
            summary = "토큰 재발급 요청",
            description = "토큰의 유효기한이 끝나면 요청이 거부되기 때문에 토큰을 새롭게 재발급 받는 API"
    )
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        return socialLoginService.reissue(request, response);
    }

    @GetMapping("/auth/token")
    public ResponseEntity<?> getTokenByAuthCode(@RequestParam("code") String code, HttpServletResponse response) {

        System.out.println("/auth/token api 진입 성공");
        return socialLoginService.getTokenByAuthCode(code, response);
    }
}
