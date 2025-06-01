package sch.travellocal.domain.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ReservationCalendarDTO {
    private String tourTitle;
    private LocalDateTime guideStartDate;
    private LocalDateTime guideEndDate;
    private String username;
}
