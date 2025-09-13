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
    // 해당 게시물에 대한 유저의 위시리스트 선택 여부
    private boolean isWishlisted;
    // 유저가 게시물 내용을 보기 위해 포인트 결제 여부 (true라면 결제한 상태이니 모자이크 해제)
    private boolean isPointPaid;
}
