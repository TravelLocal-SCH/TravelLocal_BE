package sch.travellocal.domain.place.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "place")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Place {

    // 아예 googlePlaceId를 pk로 사용하는게 적절한거 같기도
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // google map api의 place_id
    @Column(name = "google_place_id", unique = true, nullable = false)
    private String googlePlaceId;

    @Column(nullable = false)
    private double lat;

    @Column(nullable = false)
    private double lon;
}
