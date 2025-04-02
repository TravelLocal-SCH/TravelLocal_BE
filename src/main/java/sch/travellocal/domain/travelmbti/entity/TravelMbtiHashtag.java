package sch.travellocal.domain.travelmbti.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Table(name = "travel_mbti_hashtag")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelMbtiHashtag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String hashtag;

    @ManyToOne
    @JoinColumn(name = "mbti_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TravelMbti travelMbti;
}
