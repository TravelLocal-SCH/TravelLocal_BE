package sch.travellocal.domain.chat.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDto {
    private Long id;
    private Long user1Id;
    private Long user2Id;
}
