package sch.travellocal.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import sch.travellocal.auth.filter.AuthExceptionFilter;
import sch.travellocal.auth.filter.CustomLogoutFilter;
import sch.travellocal.auth.filter.JwtFilter;
import sch.travellocal.auth.oauth.CustomOauth2UserService;
import sch.travellocal.auth.oauth.CustomSuccessHandler;
import sch.travellocal.auth.util.CookieUtil;
import sch.travellocal.auth.util.JwtUtil;
import sch.travellocal.auth.util.RefreshTokenHelper;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOauth2UserService customOauth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final ObjectMapper objectMapper;
    private final RefreshTokenHelper refreshTokenHelper;
    private final CookieUtil cookieUtil;
    private final JwtUtil jwtUtil;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        http.csrf(auth -> auth.disable());

        http.logout(auth -> auth.disable());

        http.formLogin(auth -> auth.disable());

        http.httpBasic(auth -> auth.disable());

        http.addFilterBefore(new AuthExceptionFilter(objectMapper), LogoutFilter.class);

        http.addFilterAt(new CustomLogoutFilter(refreshTokenHelper, cookieUtil, jwtUtil, objectMapper), LogoutFilter.class);

        http.addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        http.oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                        .userService(customOauth2UserService))
                .successHandler(customSuccessHandler)
        );

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/reissue").permitAll()
                // .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                //.anyRequest().authenticated()
                .anyRequest().permitAll()
        );

        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Set-Cookie"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}