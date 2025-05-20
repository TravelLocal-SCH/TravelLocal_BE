package sch.travellocal.domain.tourprogram.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import sch.travellocal.domain.tourprogram.dto.TourProgramScheduleDto;

import java.util.List;

@Getter
@NoArgsConstructor
public class SaveTourProgramRequestDto {

    private String title;
    private String description;
    private int guidePrice;
    private String region;
    private String thumbnailUrl;
    private List<String> hashtags;
    private List<TourProgramScheduleDto> schedules;
}
