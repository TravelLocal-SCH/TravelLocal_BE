package sch.travellocal.domain.reservation.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import sch.travellocal.domain.reservation.enums.RequestStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReservationRequestCalendarResponse {
    private Long id;
    private String tourProgramTitle;  // 투어 프로그램명
    private LocalDateTime guideStartDate;
    private LocalDateTime guideEndDate;
    private String username;
    private int numOfPeople;
    private RequestStatus requestStatus;

    public static ReservationRequestCalendarResponse from(ReservationRequest reservation) {
        return new ReservationRequestCalendarResponse(
                reservation.getId(),
                reservation.getTourProgram().getTitle(),
                reservation.getGuideStartDate(),
                reservation.getGuideEndDate(),
                reservation.getUser().getUsername(),
                reservation.getNumOfPeople(),
                reservation.getRequestStatus()
        );
    }
}
