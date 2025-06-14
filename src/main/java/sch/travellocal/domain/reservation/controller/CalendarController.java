package sch.travellocal.domain.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sch.travellocal.domain.reservation.dto.CalendarStatusDTO;
import sch.travellocal.domain.reservation.dto.ReservationCalendarDTO;
import sch.travellocal.domain.reservation.enums.RequestStatus;
import sch.travellocal.domain.reservation.service.ReservationService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calendar")
public class CalendarController {
    private final ReservationService reservationService;


    // 캘린더 전체화면에서의 예약 상태
    @GetMapping("/status")
    public ResponseEntity<List<CalendarStatusDTO>> getUserReservationSummary(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        List<CalendarStatusDTO> result = reservationService.getReservationDatesWithStatusForUser(start, end);
        return ResponseEntity.ok(result);
    }

    // 사용자 예약 상세 내역
    @GetMapping("/my-reservations")
    public List<ReservationCalendarDTO> getMyReservations(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end
    ) {
        return reservationService.getMyReservations(start, end);
    }


    // 가이드 입장에서의 예약 상세 내역
    @GetMapping("/received")
    public List<ReservationCalendarDTO> getReceivedReservationsAsGuide(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end
    ) {
        return reservationService.getReceivedReservationsAsGuide(start, end);
    }
}