package sch.travellocal.domain.travelmbti.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "travel_mbti_region")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelMbtiRegion {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String region;

    @ManyToOne
    @JoinColumn(name = "mbti_id", nullable = false)
    private TravelMbti travelMbti;
}
