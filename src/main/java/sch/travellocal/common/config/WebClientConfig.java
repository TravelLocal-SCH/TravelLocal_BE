package sch.travellocal.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient googleWebClient() {
        return WebClient.builder()
                .baseUrl("https://maps.googleapis.com")
                .build();
    }

    @Bean
    public WebClient tourApiWebClient() {
        return WebClient.builder()
                .baseUrl("https://apis.data.go.kr/B551011/KorService2")
                .build();
    }
}
