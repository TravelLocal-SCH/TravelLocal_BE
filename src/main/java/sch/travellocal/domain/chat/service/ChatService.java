package sch.travellocal.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sch.travellocal.common.exception.custom.ApiException;
import sch.travellocal.common.exception.error.ErrorCode;
import sch.travellocal.domain.chat.dto.ChatRoomDto;
import sch.travellocal.domain.chat.dto.MessageDto;
import sch.travellocal.domain.chat.entity.ChatRoom;
import sch.travellocal.domain.chat.entity.Message;
import sch.travellocal.domain.chat.repository.ChatRoomRepository;
import sch.travellocal.domain.chat.repository.MessageRepository;
import sch.travellocal.domain.user.entity.User;
import sch.travellocal.domain.user.repository.UserRepository;
import sch.travellocal.domain.user.service.SecurityUserService;

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
    private final SecurityUserService securityUserService;


// 채팅방 생성 및 입장
    public ChatRoomDto getOrCreateChatRoom(Long otherUserId) {
        User currentUser = securityUserService.getUserByJwt(); //사용자 아이디
        Long currentUserId = currentUser.getId(); // 가이드 아이디

        if (currentUserId.equals(otherUserId)) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "자기 자신과는 채팅할 수 없습니다.");
        }

        // 기존 채팅방 조회 (user1, user2 순서 상관 없이)
        Optional<ChatRoom> existingRoom = chatRoomRepository.findByUsers(currentUserId, otherUserId);

        ChatRoom room = existingRoom.orElseGet(() -> {
            User otherUser = userRepository.findById(otherUserId)
                    .orElseThrow(() -> new ApiException(ErrorCode.DATABASE_ERROR, "상대 유저가 존재하지 않습니다."));

            return chatRoomRepository.save(ChatRoom.builder()
                    .user1(currentUser)
                    .user2(otherUser)
                    .build());
        });

        return ChatRoomDto.builder()
                .id(room.getId())
                .user1Id(room.getUser1().getId())
                .user2Id(room.getUser2().getId())
                .build();
    }



    // 채팅방 목록 조회 서비스 로직
    public List<ChatRoomDto> getChatRooms(Long otherUserId) {
        User currentUser = securityUserService.getUserByJwt();
        System.out.println("현재 로그인 유저 ID: " + currentUser.getId());
        System.out.println("otherUserId: " + otherUserId);

        userRepository.findById(otherUserId)
                .orElseThrow(() -> new ApiException(ErrorCode.DATABASE_ERROR, "상대 유저를 찾을 수 없습니다."));

        List<ChatRoom> currentUserRooms = chatRoomRepository.findByUser(currentUser.getId());
        System.out.println("현재 사용자가 포함된 채팅방 수: " + currentUserRooms.size());

        List<ChatRoom> filteredRooms = currentUserRooms.stream()
                .filter(room -> room.getUser1().getId().equals(otherUserId) || room.getUser2().getId().equals(otherUserId))
                .collect(Collectors.toList());

        System.out.println("상대 유저가 포함된 채팅방 수: " + filteredRooms.size());

        return filteredRooms.stream()
                .map(room -> ChatRoomDto.builder()
                        .id(room.getId())
                        .user1Id(room.getUser1().getId())
                        .user2Id(room.getUser2().getId())
                        .build())
                .collect(Collectors.toList());
    }

    // 메세지 내역 조회 서비스 로직
    public List<MessageDto> getMessages(Long roomId) {
        User currentUser = securityUserService.getUserByJwt();

        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "채팅방이 존재하지 않습니다."));

        boolean isParticipant = room.getUser1().getId().equals(currentUser.getId()) ||
                room.getUser2().getId().equals(currentUser.getId());

        if (!isParticipant) {
            throw new ApiException(ErrorCode.FORBIDDEN, "이 채팅방에 접근 권한이 없습니다.");
        }

        return messageRepository.findByChatRoomIdOrderByCreatedAtAsc(roomId).stream()
                .map(msg -> MessageDto.builder()
                        .id(msg.getId())
                        .userId(msg.getUser().getId())
                        .message(msg.getMessage())
                        .createdAt(msg.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }




    // 메세지 저장 서비스 로직
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
