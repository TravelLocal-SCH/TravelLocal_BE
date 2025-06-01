package sch.travellocal.domain.payment.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Slf4j
@Service
public class IamportTokenService {

    private final WebClient webClient;

    @Value("${iamport.api-key}")
    private String apiKey;

    @Value("${iamport.api-secret}")
    private String apiSecret;

    @Getter
    private String accessToken;

    private Instant tokenExpiryTime;  // 토큰 만료 시간 저장

    public IamportTokenService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.iamport.kr")
                .build();
    }

    // 토큰을 가져오는 메서드 (유효기간 체크 + 재발급 처리)
    public synchronized String getToken() {
        if (accessToken == null || Instant.now().isAfter(tokenExpiryTime)) {
            log.info("아임포트 토큰이 없거나 만료되어 새로 발급 요청합니다.");
            fetchNewToken();
        } else {
            log.info("아임포트 토큰 유효, 기존 토큰 사용");
        }
        return accessToken;
    }

    // 실제 토큰 발급 요청
    private void fetchNewToken() {
        try {
            TokenResponse tokenResponse = webClient.post()
                    .uri("/users/getToken")
                    .bodyValue(new TokenRequest(apiKey, apiSecret))
                    .retrieve()
                    .bodyToMono(TokenResponse.class)
                    .block();

            if (tokenResponse != null && tokenResponse.getResponse() != null) {
                this.accessToken = tokenResponse.getResponse().getAccess_token();

                // 만료시간을 현재 시간 + expires_in 초 (보통 3600초 = 1시간)
                this.tokenExpiryTime = Instant.now().plusSeconds(tokenResponse.getResponse().getExpired_at());
                log.info("새로운 아임포트 토큰 발급 완료. 만료 시간: {}", tokenExpiryTime);
            } else {
                throw new RuntimeException("아임포트 토큰 발급 실패: 응답 없음");
            }
        } catch (Exception e) {
            log.error("아임포트 토큰 발급 중 오류 발생", e);
            throw new RuntimeException("아임포트 토큰 발급 중 오류: " + e.getMessage());
        }
    }

    // 내부 DTO들 (요청, 응답)
    private record TokenRequest(String imp_key, String imp_secret) {}

    private static class TokenResponse {
        private TokenData response;

        public TokenData getResponse() {
            return response;
        }

        public void setResponse(TokenData response) {
            this.response = response;
        }
    }

    private static class TokenData {
        private String access_token;
        private long expired_at;  // 만료시간: Unix timestamp (초)

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public long getExpired_at() {
            return expired_at;
        }

        public void setExpired_at(long expired_at) {
            this.expired_at = expired_at;
        }
    }
}
