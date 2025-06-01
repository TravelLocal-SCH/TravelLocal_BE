package sch.travellocal.domain.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sch.travellocal.domain.reservation.entity.ReservationRequestCalendarResponse;
import sch.travellocal.domain.reservation.service.ReservationService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
class CalendarController {

    private final ReservationService reservationRequestService;

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationRequestCalendarResponse>> getReservationsForCalendar(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        List<ReservationRequestCalendarResponse> reservations = reservationRequestService.getReservationsBetween(startDate, endDate);
        return ResponseEntity.ok(reservations);
    }
}
