package sch.travellocal.domain.chat.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

// 메세지 내역을 받고 저장을 위한 DTO

public class MessageDto {
    private Long id;
    private Long userId;
    private String message;
    private LocalDateTime createdAt;
}
