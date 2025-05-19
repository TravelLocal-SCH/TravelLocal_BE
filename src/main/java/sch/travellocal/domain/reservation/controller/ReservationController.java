package sch.travellocal.domain.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sch.travellocal.domain.payment.dto.PaymentDTO;
import sch.travellocal.domain.payment.service.PaymentService;
import sch.travellocal.domain.reservation.dto.ReservationRequestDTO;
import sch.travellocal.domain.reservation.service.ReservationService;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<?> createReservation(
            @RequestBody ReservationRequestDTO reservationDTO,
            @RequestParam String impUid
    ) {
        paymentService.verifyAndCreateReservation(request);
        return ResponseEntity.ok("예약 및 결제 완료");
    }
}
