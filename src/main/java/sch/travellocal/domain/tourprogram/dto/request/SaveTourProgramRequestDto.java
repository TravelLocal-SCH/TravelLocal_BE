package sch.travellocal.domain.tourprogram.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sch.travellocal.domain.tourprogram.dto.TourProgramScheduleDto;

import java.util.List;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class SaveTourProgramRequestDto {

    @NotBlank(message = "title 필수값입니다.")
    private String title;

    @NotBlank(message = "description 필수값입니다.")
    private String description;

    @NotNull(message = "guidePrice 필수값입니다.")
    private int guidePrice;

    @NotBlank(message = "region 필수값입니다.")
    private String region;

    private String thumbnailUrl;

    @NotEmpty(message = "최소 하나 이상의 해시태그를 등록해야 합니다.")
    @Size(min = 1, message = "최소 하나 이상의 해시태그를 등록해야 합니다.")
    private List<String> hashtags;

    @NotEmpty(message = "최소 하나 이상의 일정을 등록해야 합니다.")
    @Size(min = 1, message = "최소 하나 이상의 일정을 등록해야 합니다.")
    @Valid
    private List<TourProgramScheduleDto> schedules;
}
