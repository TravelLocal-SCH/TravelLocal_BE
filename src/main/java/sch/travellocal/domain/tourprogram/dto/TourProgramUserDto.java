package sch.travellocal.domain.tourprogram.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TourProgramUserDto {

    private Long id;
    private String name;
}
