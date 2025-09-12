package sch.travellocal.domain.place.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "place_count")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Place에 대해 count 정보를 저장하는 엔티티
public class PlaceCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "review_count", nullable = false)
    private int reviewCount;

    // 리뷰의 평점 합계 (원래 리뷰가 추가될 때마다 평균별점을 하려 했지만 그러면 계속해서 오차가 발생할거 같아서 sum을 만들고 avgRating이 필요할 때 sumRating/reviewCount로 계산)
    @Column(name = "sum_rating", nullable = false)
    private float sumRating;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;
}