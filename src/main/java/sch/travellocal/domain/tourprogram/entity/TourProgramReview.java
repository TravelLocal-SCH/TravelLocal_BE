package sch.travellocal.domain.tourprogram.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sch.travellocal.common.entity.BaseTimeEntity;
import sch.travellocal.domain.user.entity.User;

@Entity
@Table(name = "tour_program_review")
@Getter
@NoArgsConstructor
public class TourProgramReview extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private float rating;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_program_id", nullable = false)
    private TourProgram tourProgram;

    @Builder
    public TourProgramReview(float rating, String content, User user, TourProgram tourProgram) {
        this.rating = rating;
        this.content = content;
        this.user = user;
        this.tourProgram = tourProgram;
    }
}