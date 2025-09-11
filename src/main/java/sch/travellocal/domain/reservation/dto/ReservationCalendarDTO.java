package sch.travellocal.domain.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ReservationCalendarDTO {
    private Long id;
    private String tourProgramTitle;
    private LocalDateTime guideStartDate;
    private LocalDateTime guideEndDate;
    private int numOfPeople;
    private String requestStatus;
}
