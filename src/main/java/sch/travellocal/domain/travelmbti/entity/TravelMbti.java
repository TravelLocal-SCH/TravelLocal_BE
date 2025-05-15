package sch.travellocal.domain.travelmbti.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import sch.travellocal.domain.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "travel_mbti")
@NoArgsConstructor
@AllArgsConstructor
public class TravelMbti {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mbti", nullable = false)
    private String mbti;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public TravelMbti(String mbti, User user) {
        this.mbti = mbti;
        this.user = user;
    }
}
