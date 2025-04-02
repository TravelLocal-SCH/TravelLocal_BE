package sch.travellocal.domain.travelmbti.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseSimpleMbtiDTO {

    private Long mbtiId;
    private String mbti;
}
