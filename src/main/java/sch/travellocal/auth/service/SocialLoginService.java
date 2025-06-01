package sch.travellocal.auth.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sch.travellocal.auth.util.CookieUtil;
import sch.travellocal.auth.util.JwtUtil;
import sch.travellocal.auth.util.RefreshTokenHelper;
import sch.travellocal.common.exception.custom.AuthException;
import sch.travellocal.common.exception.error.ErrorCode;
import sch.travellocal.common.response.SuccessResponse;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class SocialLoginService {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RefreshTokenHelper refreshTokenHelper;
    private final long ACCESS_TOKEN_TTL = 10 * 60;
    private final long REFRESH_TOKEN_TTL = 7 * 24 * 60 * 60;

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        // 요청에서 refresh 토큰 값을 쿠키로부터 추출
        String refreshToken = cookieUtil.getCookieValue(request, "refresh");

        // 추출한 refresh 토큰이 유효한지 검증
        refreshTokenHelper.validateRefreshToken(refreshToken);

        // refresh 토큰에서 사용자 이름과 역할(role) 정보 추출
        String username = jwtUtil.getUserName(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        // 새 access 토큰과 refresh 토큰 생성
        String newAccessToken = jwtUtil.createJwt("access", username, role, ACCESS_TOKEN_TTL);
        String newRefreshToken = jwtUtil.createJwt("refresh", username, role, REFRESH_TOKEN_TTL);

        System.out.println("newAccessToken: " + newAccessToken);
        System.out.println("newRefreshToken: " + newRefreshToken);

        // 기존 refresh 토큰을 블랙리스트에 추가 후 삭제
        refreshTokenHelper.addBlacklistRefreshToken(refreshToken);
        refreshTokenHelper.deleteRefreshToken(refreshToken);

        // 새 refresh 토큰을 저장
        refreshTokenHelper.saveRefreshToken(newRefreshToken);

        // 새 access 토큰을 헤더에 설정하고, 새 refresh 토큰을 쿠키에 저장
        response.setHeader("Authorization", "Bearer " + newAccessToken);
        response.addCookie(cookieUtil.createCookie("refresh", newRefreshToken, (int) REFRESH_TOKEN_TTL));

        return ResponseEntity.ok(SuccessResponse.ok("Access token reissued"));
    }

    public ResponseEntity<?> getTokenByAuthCode(String code, HttpServletResponse response) {

        System.out.println("getTokenByAuthCode1 code: " + code);
        String redisKey = "auth_code:" + code;
        String tokenJson = redisTemplate.opsForValue().get(redisKey);
        if (tokenJson == null) {
            throw new AuthException(ErrorCode.INVALID_AUTH_CODE);
        }

        redisTemplate.delete(redisKey);
        String accessToken = "";
        String refreshToken = "";

        System.out.println("getTokenByAuthCode2 tokenJson: " + tokenJson);

        try {
            Map<String,String> tokenMap = objectMapper.readValue(tokenJson, new TypeReference<>() {});
            accessToken = tokenMap.get("accessToken");
            refreshToken = tokenMap.get("refreshToken");
        } catch (Exception e) {
            throw new AuthException(ErrorCode.TRANSFORMATION_ERROR);
        }
        // 새 access 토큰을 헤더에 설정하고, 새 refresh 토큰을 쿠키에 저장
        response.setHeader("Authorization", "Bearer " + accessToken);
        response.addCookie(cookieUtil.createCookie("refresh", refreshToken, (int) REFRESH_TOKEN_TTL));

        System.out.println("AuthCode3 accessToken: " + accessToken);

        return ResponseEntity.ok(SuccessResponse.ok("Access token reissued"));
    }
}
