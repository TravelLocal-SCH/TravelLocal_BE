package sch.travellocal.domain.point.dto.response;

import lombok.Builder;
import lombok.Getter;
import sch.travellocal.domain.point.enums.PointTransactionActionType;

import java.time.LocalDateTime;

@Getter
@Builder
public class PointHistoryResponseDto {

    private Long historyId;
    private PointTransactionActionType actionType;
    private String actionSubject;
    private int pointAmount;
    private int remainPointAfter;
    private LocalDateTime createdAt;
}
