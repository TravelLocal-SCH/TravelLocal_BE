package sch.travellocal.domain.tourprogram.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "program_count")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgramCount {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "review_count", nullable = false)
    private int reviewCount;

    @Column(name = "wishlist_count", nullable = false)
    private int wishlistCount;

    @OneToOne
    @JoinColumn(name = "tour_program_id", nullable = false)
    private TourProgram tourProgram;
}
