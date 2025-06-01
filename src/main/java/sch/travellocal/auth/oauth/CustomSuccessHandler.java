package sch.travellocal.auth.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import sch.travellocal.auth.util.CookieUtil;
import sch.travellocal.auth.util.JwtUtil;
import sch.travellocal.auth.util.RefreshTokenHelper;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final RefreshTokenHelper refreshTokenHelper;
    private ObjectMapper objectMapper;
    private final long ACCESS_TOKEN_TTL = 7 * 24 * 60 * 60; //10 * 60;
    private final long REFRESH_TOKEN_TTL = 7 * 24 * 60 * 60;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, SecurityException {

        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority authority = iterator.next();
        String role = authority.getAuthority();

        String accessToken = jwtUtil.createJwt("access", username, role, ACCESS_TOKEN_TTL);
        String refreshToken = jwtUtil.createJwt("refresh", username, role, REFRESH_TOKEN_TTL);

        // 추후에 삭제 예정
        System.out.println("accessToken: Bearer " + accessToken);
        System.out.println("refreshToken: " + refreshToken);
        // 여기까지 삭제

        refreshTokenHelper.saveRefreshToken(refreshToken);

        // 5. 인증 코드 생성 (UUID)
        String authCode = UUID.randomUUID().toString();

        // 6. Redis에 인증 코드 → access/refresh token 매핑
        String redisKey = "auth_code:" + authCode;

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("accessToken", accessToken);
        tokenMap.put("refreshToken", refreshToken);
        String tokenValue = objectMapper.writeValueAsString(tokenMap);

        redisTemplate.opsForValue().set(redisKey, tokenValue, 5, TimeUnit.MINUTES);

        // 7. React Native 앱으로 딥링크 리디렉션
        String redirectUrl = "travellocal://login/callback?code=" + authCode;

        System.out.println("Redirect 시작");

        response.sendRedirect(redirectUrl);
    }
}
