package sch.travellocal.domain.travelmbti.dto.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class TravelMbtiDTO {

    private String travelMbti;
    private List<String> hashtags;
    private List<String> regions;
}
