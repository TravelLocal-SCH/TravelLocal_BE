package sch.travellocal.domain.tourprogram.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tour_program_schedule")
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

    @Column(nullable = false)
    private String address;

    @Column(name = "place_description")
    private String placeDescription;

    @Column(name = "travel_time")
    private LocalDateTime travelTime;

    @ManyToOne
    @JoinColumn(name = "tour_program_id", nullable = false)
    private TourProgram tourProgram;
}
