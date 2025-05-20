package sch.travellocal.domain.tourprogram.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tour_program_count")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourProgramCount {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "review_count", nullable = false)
    private int reviewCount;

    @Column(name = "wishlist_count", nullable = false)
    private int wishlistCount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_program_id", nullable = false)
    private TourProgram tourProgram;
}
