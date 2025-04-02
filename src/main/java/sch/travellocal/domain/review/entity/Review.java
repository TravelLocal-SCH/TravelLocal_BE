package sch.travellocal.domain.review.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;
import sch.travellocal.common.entity.BaseTimeEntity;
import sch.travellocal.domain.tourprogram.entity.TourProgram;
import sch.travellocal.domain.user.entity.User;


@Entity
@Table(name = "review")
@NoArgsConstructor
public class Review extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int rating;

    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "tour_program_id", nullable = false)
    private TourProgram tourProgram;

    @Builder
    public Review(int rating, String content, User user, TourProgram tourProgram) {
        this.rating = rating;
        this.content = content;
        this.user = user;
        this.tourProgram = tourProgram;
    }
}
