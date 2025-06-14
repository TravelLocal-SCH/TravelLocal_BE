package sch.travellocal.domain.chat.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

// 채팅방 생성 데이터를 받기 위한 DTO

public class ChatRoomDto {
    private Long id;
    private Long user1Id;
    private Long user2Id;
}
