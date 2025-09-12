package sch.travellocal.domain.point.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PointTransactionActionType {

    EARN, // 증가
    USE, // 감소
    EXPIRE, // 만료로 인한 감소
    ROLLBACK
}
