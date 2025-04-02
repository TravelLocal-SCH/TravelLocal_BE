package sch.travellocal.domain.travelmbti.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ResponseMbtiListDTO {

    private List<String> mbtiList;
}
