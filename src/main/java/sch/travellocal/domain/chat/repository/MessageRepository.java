package sch.travellocal.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sch.travellocal.domain.chat.entity.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatRoomIdOrderByCreatedAtAsc(Long chatRoomId);
}
