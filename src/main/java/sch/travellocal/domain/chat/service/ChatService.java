package sch.travellocal.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sch.travellocal.domain.chat.dto.ChatRoomDto;
import sch.travellocal.domain.chat.dto.MessageDto;
import sch.travellocal.domain.chat.entity.ChatRoom;
import sch.travellocal.domain.chat.entity.Message;
import sch.travellocal.domain.chat.repository.ChatRoomRepository;
import sch.travellocal.domain.chat.repository.MessageRepository;
import sch.travellocal.domain.user.entity.User;
import sch.travellocal.domain.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public List<ChatRoomDto> getChatRooms(Long userId) {
        return chatRoomRepository.findByUser(userId).stream()
                .map(room -> ChatRoomDto.builder()
                        .id(room.getId())
                        .user1Id(room.getUser1().getId())
                        .user2Id(room.getUser2().getId())
                        .build())
                .collect(Collectors.toList());
    }

    public List<MessageDto> getMessages(Long roomId) {
        return messageRepository.findByChatRoomIdOrderByCreatedAtAsc(roomId).stream()
                .map(msg -> MessageDto.builder()
                        .id(msg.getId())
                        .userId(msg.getUser().getId())
                        .message(msg.getMessage())
                        .createdAt(msg.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public ChatRoomDto getOrCreateChatRoom(Long user1Id, Long user2Id) {
        Optional<ChatRoom> existing = chatRoomRepository.findByUsers(user1Id, user2Id);
        if (existing.isPresent()) {
            ChatRoom room = existing.get();
            return ChatRoomDto.builder()
                    .id(room.getId())
                    .user1Id(room.getUser1().getId())
                    .user2Id(room.getUser2().getId())
                    .build();
        }

        User user1 = userRepository.findById(user1Id).orElseThrow();
        User user2 = userRepository.findById(user2Id).orElseThrow();
        ChatRoom room = chatRoomRepository.save(ChatRoom.builder()
                .user1(user1)
                .user2(user2)
                .build());
        return ChatRoomDto.builder()
                .id(room.getId())
                .user1Id(user1Id)
                .user2Id(user2Id)
                .build();
    }

    public Message saveMessage(Long roomId, Long userId, String content) {
        ChatRoom room = chatRoomRepository.findById(roomId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        Message message = Message.builder()
                .chatRoom(room)
                .user(user)
                .message(content)
                .build();
        return messageRepository.save(message);
    }
}
