package sch.travellocal.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import sch.travellocal.auth.service.SocialLoginService;

@RestController
@RequiredArgsConstructor
public class SocialLoginController {

    private final SocialLoginService socialLoginService;

    @GetMapping("/home")
    public ResponseEntity<String> home() {

        return ResponseEntity.ok("home");
    }

    /**
     * token 재발급 API
     */
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        return socialLoginService.reissue(request, response);
    }
}
