package sch.travellocal.domain.chat.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private Long id;
    private Long userId;
    private String message;
    private LocalDateTime createdAt;
}
