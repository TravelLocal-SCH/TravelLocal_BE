package sch.travellocal.domain.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.sql.rowset.serial.SerialStruct;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ReservationCalendarDTO {
    private Long id;
    private String tourProgramTitle;
    private LocalDateTime guideStartDate;
    private LocalDateTime guideEndDate;
    private int numOfPeople;
    private String requestStatus;

    private String role; // "GUIDE" or "USER"
}
