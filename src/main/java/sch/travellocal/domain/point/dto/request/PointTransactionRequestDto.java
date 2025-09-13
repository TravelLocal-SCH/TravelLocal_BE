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

    // (ex. 게시물 내용 모자이크 해제를 위해 포인트 결제를 했다면 PointTransactionSubjectType: CONTENT)
    @NotNull
    private PointTransactionSubjectType actionSubject;

    // PointTransactionSubjectType에 대한 엔티티_id (ex. 게시물_id == tour_program_id)
    private Long targetId;
}
