package sch.travellocal.domain.tourprogram.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import sch.travellocal.common.entity.BaseTimeEntity;
import sch.travellocal.domain.user.entity.User;

@Entity
@Table(name = "tour_program")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourProgram extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    private String description;

    @Column(name = "guide_price", nullable = false)
    private int guidePrice;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public TourProgram(String title, String description, int guidePrice, User user) {
        this.title = title;
        this.description = description;
        this.guidePrice = guidePrice;
        this.user = user;
    }
}
