package sch.travellocal.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sch.travellocal.domain.chat.dto.ChatRoomDto;
import sch.travellocal.domain.chat.dto.MessageDto;
import sch.travellocal.domain.chat.service.ChatService;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
//

    // 채팅방 조회
    @GetMapping("/rooms")
    public List<ChatRoomDto> getChatRooms(@RequestParam Long userId) {
        return chatService.getChatRooms(userId);
    }

    // 채팅방 안에 있는 메세지 내역 조회
    @GetMapping("/rooms/{roomId}/messages")
    public List<MessageDto> getMessages(@PathVariable Long roomId) {
        return chatService.getMessages(roomId);
    }

    // 채팅방 생성 및 입장(이미 있다면 그대로 입장)
    @PostMapping("/rooms")
    public ChatRoomDto enterOrCreateChatRoom(@RequestParam Long userId) {
        return chatService.getOrCreateChatRoom(userId);
    }



}
