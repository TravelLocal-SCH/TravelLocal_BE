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

    @GetMapping("/rooms")
    public List<ChatRoomDto> getChatRooms(@RequestParam Long userId) {
        return chatService.getChatRooms(userId);
    }

    @GetMapping("/rooms/{roomId}/messages")
    public List<MessageDto> getMessages(@PathVariable Long roomId) {
        return chatService.getMessages(roomId);
    }

    @PostMapping("/rooms")
    public ChatRoomDto createChatRoom(@RequestParam Long user1Id, @RequestParam Long user2Id) {
        return chatService.getOrCreateChatRoom(user1Id, user2Id);
    }
}
