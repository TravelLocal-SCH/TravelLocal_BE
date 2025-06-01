package sch.travellocal.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sch.travellocal.auth.oauth.CustomOAuth2User;
import sch.travellocal.auth.util.JwtUtil;
import sch.travellocal.domain.user.dto.UserDTO;
import sch.travellocal.domain.user.enums.UserRole;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = token.split(" ")[1];

        jwtUtil.validateToken(accessToken);

        String username = jwtUtil.getUserName(accessToken);
        String role = jwtUtil.getRole(accessToken);

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(
                UserDTO.builder()
                        .name(username)
                        .username(username)
                        .role(UserRole.valueOf(role))
                        .build());

        System.out.println("jwtFilter - username: " + username);
        System.out.println("jwtFilter - username: " + customOAuth2User.getUsername());

        Authentication authentication = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        System.out.println("jwtFilter 실행됨" + SecurityContextHolder.getContext().getAuthentication().getName());

        filterChain.doFilter(request, response);
    }
}
