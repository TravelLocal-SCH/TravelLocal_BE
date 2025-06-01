package sch.travellocal.domain.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sch.travellocal.domain.payment.service.PaymentService;
import sch.travellocal.domain.reservation.dto.ReservationAndPaymentRequestDTO;
import sch.travellocal.domain.reservation.dto.ReservationRequestDTO;
import sch.travellocal.domain.reservation.service.ReservationService;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final PaymentService paymentService;

    /**
     * 예약 및 결제 처리 (프론트에서 예약 + 결제 결과를 함께 보냄)
     */
    @PostMapping
    public ResponseEntity<String> createReservationAndPayment(
            @RequestBody ReservationAndPaymentRequestDTO request
    ) {
        try {
            ReservationRequestDTO reservationDTO = request.getReservation();
            String impUid = request.getImpUid();
            String merchantUid = request.getMerchantUid();
            Long userId = request.getUserId();

            // 예약 처리
            Long reservationRequestId = reservationService.processReservation(reservationDTO, impUid);

            // 결제 처리
            paymentService.processPayment(impUid, merchantUid, reservationRequestId, userId);

            return ResponseEntity.ok("예약 및 결제 완료");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("예약 또는 결제 실패: " + e.getMessage());
        }
    }

}
