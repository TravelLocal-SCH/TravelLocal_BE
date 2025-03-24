package sch.travellocal.domain.tourprogram.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import sch.travellocal.domain.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "tour_program_wishlist")
@NoArgsConstructor
public class TourProgramWishlist {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "tour_program_id", nullable = false)
    private TourProgram tourProgram;

    @Builder
    public TourProgramWishlist(User user, TourProgram tourProgram) {
        this.user = user;
        this.tourProgram = tourProgram;
    }
}
