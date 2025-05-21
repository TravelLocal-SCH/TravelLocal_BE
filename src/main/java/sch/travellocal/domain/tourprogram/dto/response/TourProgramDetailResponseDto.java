package sch.travellocal.domain.tourprogram.dto.response;

import lombok.Builder;
import lombok.Getter;
import sch.travellocal.domain.tourprogram.dto.TourProgramScheduleDto;
import sch.travellocal.domain.tourprogram.dto.TourProgramUserDto;

import java.util.List;

@Getter
@Builder
public class TourProgramDetailResponseDto {

    // 게시물 정보
    private Long TourProgramId;
    private String title;
    private String region;
    private String description;
    private int guidePrice;
    private String thumbnailUrl;
    private List<String> hashtags;
    private List<TourProgramScheduleDto> schedules;
    // 작성자 정보
    private TourProgramUserDto user;
    // count
    private int reviewCount;
    private int wishlistCount;
}
