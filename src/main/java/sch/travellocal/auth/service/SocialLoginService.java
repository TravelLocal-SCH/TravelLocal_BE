package sch.travellocal.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sch.travellocal.auth.util.CookieUtil;
import sch.travellocal.auth.util.JwtUtil;
import sch.travellocal.auth.util.RefreshTokenHelper;
import sch.travellocal.common.response.SuccessResponse;

@Service
@RequiredArgsConstructor
public class SocialLoginService {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RefreshTokenHelper refreshTokenHelper;
    private final long ACCESS_TOKEN_TTL = 10 * 60;
    private final long REFRESH_TOKEN_TTL = 7 * 24 * 60 * 60;

    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = cookieUtil.getCookieValue(request, "refresh");

        refreshTokenHelper.validateRefreshToken(refreshToken);

        String username = jwtUtil.getUserName(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        String newAccessToken = jwtUtil.createJwt("access", username, role, ACCESS_TOKEN_TTL);
        String newRefreshToken = jwtUtil.createJwt("refresh", username, role, REFRESH_TOKEN_TTL);

        refreshTokenHelper.addBlacklistRefreshToken(refreshToken);
        refreshTokenHelper.deleteRefreshToken(refreshToken);

        refreshTokenHelper.saveRefreshToken(newRefreshToken);

        response.setHeader("Authorization", "Bearer " + newAccessToken);
        response.addCookie(cookieUtil.createCookie("refresh", newRefreshToken, (int) REFRESH_TOKEN_TTL));

        return ResponseEntity.ok(SuccessResponse.ok("Access token reissued"));
    }
}
