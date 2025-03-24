package sch.travellocal.domain.travelmbti.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import sch.travellocal.domain.user.entity.User;

@Entity
@Table(name = "travel_mbti")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelMbti {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mbti_type", nullable = false)
    private String mbtiType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
