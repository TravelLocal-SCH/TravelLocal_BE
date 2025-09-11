package sch.travellocal.auth.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    /**
     * 보안 강화된 쿠키 생성 메서드
     *
     * 이 메서드는 주로 Refresh Token과 같은 민감 정보를 쿠키에 저장할 때 사용한다.
     *
     * 적용된 보안 요소:
     * - HttpOnly: JavaScript에서 접근 불가 → XSS 공격 방지
     * - Secure: HTTPS 환경에서만 쿠키 전송 → 중간자 공격(MITM) 방지
     * - Path="/": 쿠키가 모든 경로에서 사용 가능하도록 지정
     * - SameSite (설명): CSRF 공격을 방지하기 위한 설정
     *   ※ Java Servlet API에서는 직접 지원되지 않아 수동으로 헤더를 설정해야 할 수 있음
     *
     * 보안 위협 해결 요약:
     * - XSS: HttpOnly
     * - CSRF: SameSite 설정 필요 (Strict 또는 Lax)
     * - HTTPS 미적용 시 탈취 가능성 존재 → Secure 설정으로 방지
     */
    public Cookie createCookie(String key, String value, int expiredS) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(expiredS);
        cookie.setPath("/");

        // XSS 대응 - JavaScript로 접근 차단
        cookie.setHttpOnly(true);

        // HTTPS 연결에서만 전송되도록 설정 (MITM 방지)
        cookie.setSecure(true);

        return cookie;
    }

    public String getCookieValue(HttpServletRequest request, String name) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
