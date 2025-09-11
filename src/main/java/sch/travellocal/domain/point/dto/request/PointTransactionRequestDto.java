package sch.travellocal.domain.point.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import sch.travellocal.domain.point.enums.PointTransactionActionType;
import sch.travellocal.domain.point.enums.PointTransactionSubjectType;

@Getter
public class PointTransactionRequestDto {

    @NotNull(message = "사용할 포인트 양은 필수입니다.")
    @Positive(message = "사용할 포인트는 0보다 커야 합니다.")
    private int amount;

    @NotNull
    private PointTransactionActionType actionType;

    @NotNull
    private PointTransactionSubjectType actionSubject;

    private Long targetId;
}
