package sch.travellocal.domain.place.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sch.travellocal.common.entity.BaseTimeEntity;
import sch.travellocal.domain.user.entity.User;

@Entity
@Table(name = "place_review")
@Getter
@NoArgsConstructor
public class PlaceReview extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private float rating;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Builder
    public PlaceReview(Place place, User user, String content, float rating) {
        this.place = place;
        this.user = user;
        this.content = content;
        this.rating = rating;
    }
}