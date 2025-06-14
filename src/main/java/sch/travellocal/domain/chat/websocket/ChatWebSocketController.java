package sch.travellocal.domain.chat.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import sch.travellocal.domain.chat.dto.MessageDto;
import sch.travellocal.domain.chat.entity.Message;
import sch.travellocal.domain.chat.service.ChatService;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload MessageDto messageDto) {

        System.out.println("Received message: roomId=" + messageDto.getId() + ", userId=" + messageDto.getUserId() + ", message=" + messageDto.getMessage());

        //메세지 저장
        Message saved = chatService.saveMessage(messageDto.getId(), messageDto.getUserId(), messageDto.getMessage());

        //메세지 변환
        MessageDto response = MessageDto.builder()
                .id(saved.getId())
                .userId(saved.getUser().getId())
                .message(saved.getMessage())
                .createdAt(saved.getCreatedAt())
                .build();

        //메세지 전송
        messagingTemplate.convertAndSend("/topic/chatroom/" + messageDto.getId(), response);
    }
}
