package sch.travellocal.domain.tourprogram.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class WishlistProgramDto {

    // 유저의 위시리스트 클릭 시 보여질 요소
    private Long tourProgramId;
    private String thumbnailUrl;
    private String title;
    private String region;
    private int guidePrice;
    private int wishlistCount;
}
