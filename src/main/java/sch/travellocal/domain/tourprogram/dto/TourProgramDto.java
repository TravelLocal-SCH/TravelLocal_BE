package sch.travellocal.domain.tourprogram.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourProgramDto {

    private Long id;
    private String title;
    private String description;
    private int guidePrice;
    private List<String> hashtags;
    private String region;
    private String thumbnailUrl;
}
