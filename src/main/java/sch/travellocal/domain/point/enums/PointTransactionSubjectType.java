package sch.travellocal.domain.point.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PointTransactionSubjectType {

    MISSION("미션"),
    PROMOTION("프로모션"),
    CONTENT("컨텐츠"),
    ADMIN("관리자");

    private final String description;
}
