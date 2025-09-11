package sch.travellocal.domain.tourprogram.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sch.travellocal.domain.place.entity.Place;

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

    // 작성자가 작성한 장소에 대한 설명
    @Column(name = "place_description")
    private String placeDescription;

    @Column(name = "travel_time")
    private Integer travelTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_program_id", nullable = false)
    private TourProgram tourProgram;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;
}
