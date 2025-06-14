package sch.travellocal.domain.chat.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import sch.travellocal.domain.chat.entity.ChatRoom;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT c FROM ChatRoom c WHERE c.user1.id = :userId OR c.user2.id = :userId")
    List<ChatRoom> findByUser(@Param("userId") Long userId);

    @Query("SELECT r FROM ChatRoom r WHERE " +
            "(r.user1.id = :userId1 AND r.user2.id = :userId2) OR " +
            "(r.user1.id = :userId2 AND r.user2.id = :userId1)")
    Optional<ChatRoom> findByUsers(@Param("userId1") Long userId1, @Param("userId2") Long userId2);


    @Query("SELECT c FROM ChatRoom c WHERE (c.user1.id = :userId1 AND c.user2.id = :userId2) OR (c.user1.id = :userId2 AND c.user2.id = :userId1)")
    List<ChatRoom> findByTwoUsers(@Param("userId1") Long userId1, @Param("userId2") Long userId2);


}
