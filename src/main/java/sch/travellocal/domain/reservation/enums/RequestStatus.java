package sch.travellocal.domain.reservation.enums;

public enum RequestStatus {

    PENDING,        // 예약 요청 대기 중
    ACCEPTED,       // 가이드 수락
    REJECTED,       // 가이드 거절
    CANCELLED_BY_USER,  // 사용자가 예약 취소
    CANCELLED_BY_GUIDE, // 가이드가 예약 취소
    COMPLETED       // 투어 완료됨
}
