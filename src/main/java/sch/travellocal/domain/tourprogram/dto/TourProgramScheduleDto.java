package sch.travellocal.domain.tourprogram.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourProgramScheduleDto {

    @NotNull(message = "day 필수값입니다.")
    private int day;

    @NotNull(message = "scheduleSequence 필수값입니다.")
    private int scheduleSequence;

    @NotBlank(message = "googlePlaceId는 필수값입니다.")
    private String googlePlaceId;

    @NotBlank(message = "placeName 필수값입니다.")
    private String placeName;

    @NotNull(message = "lat 필수값입니다.")
    private Double lat;

    @NotNull(message = "lon 필수값입니다.")
    private Double lon;

    @NotBlank(message = "placeDescription 필수값입니다.")
    private String placeDescription;

    @NotNull(message = "travelTime 필수값입니다.")
    private int travelTime;
}
