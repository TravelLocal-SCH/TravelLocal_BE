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

    @GetMapping("/home")
    @Operation(
            summary = "테스트 API",
            description = "테스트용이며 \"helloWorld\"를 requestBody에 포함하여 요청을 보내면 \"home\"을 응답으로 제공한다."
    )
    public ResponseEntity<String> home(@RequestBody String helloWorld) {

        if (helloWorld.equals("helloWorld")) {
            return ResponseEntity.badRequest().body("you should request \"helloWorld\"");
        }
        return ResponseEntity.ok("home");
    }

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
