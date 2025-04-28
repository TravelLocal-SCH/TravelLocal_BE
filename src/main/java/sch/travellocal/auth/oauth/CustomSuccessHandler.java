package sch.travellocal.auth.oauth;

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
import java.util.Collection;
import java.util.Iterator;

@Component
@AllArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final RefreshTokenHelper refreshTokenHelper;
    private final long ACCESS_TOKEN_TTL = 10 * 60;
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

        response.setHeader("Authorization", "Bearer " + accessToken);
        response.addCookie(cookieUtil.createCookie("refresh", refreshToken, (int) REFRESH_TOKEN_TTL));
        response.sendRedirect("http://localhost:3000/");
    }
}
