package sch.travellocal.domain.tourprogram.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourProgramScheduleDto {

    private int day;
    private int scheduleSequence;
    private String placeName;
    private Double lat;
    private Double lon;
    private String placeDescription;
    private int travelTime;
}
