package sch.travellocal.domain.travelmbti.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ResponseDetailMbtiDTO {

    private String mbti;
    private List<String> hashtags;
    private List<String> regions;
}
