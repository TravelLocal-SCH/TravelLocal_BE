package sch.travellocal.domain.tourprogram.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tour_program_schedule",
    uniqueConstraints = {
            @UniqueConstraint(columnNames = {"day", "schedule_sequence", "tour_program_id"})
    })
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourProgramSchedule {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int day;

    @Column(name = "schedule_sequence", nullable = false)
    private int scheduleSequence;

    @Column(name = "place_name", nullable = false)
    private String placeName;

    // 위도
    @Column
    private Double lat;

    // 경도
    @Column
    private Double lon;

    @Column(name = "place_description")
    private String placeDescription;

    @Column(name = "travel_time")
    private Integer travelTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_program_id", nullable = false)
    private TourProgram tourProgram;
}
