package sch.travellocal.domain.chat.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import sch.travellocal.domain.chat.entity.ChatRoom;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT c FROM ChatRoom c WHERE c.user1.id = :userId OR c.user2.id = :userId")
    List<ChatRoom> findByUser(@Param("userId") Long userId);

    @Query("SELECT c FROM ChatRoom c WHERE (c.user1.id = :u1 AND c.user2.id = :u2) OR (c.user1.id = :u2 AND c.user2.id = :u1)")
    Optional<ChatRoom> findByUsers(@Param("u1") Long u1, @Param("u2") Long u2);
}
