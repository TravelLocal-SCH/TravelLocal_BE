package sch.travellocal.domain.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
@Getter
@AllArgsConstructor
public class CalendarStatusDTO {


    private Long reservationId;
    private LocalDate date;
    private String status;

}
