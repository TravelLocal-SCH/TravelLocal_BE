//package sch.travellocal.domain.reservation.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import sch.travellocal.domain.payment.dto.PaymentDTO;
//import sch.travellocal.domain.payment.service.PaymentService;
//import sch.travellocal.domain.reservation.dto.ReservationRequestDTO;
//import sch.travellocal.domain.reservation.service.ReservationService;
//
//@RestController
//@RequestMapping("/api/reservations")
//@RequiredArgsConstructor
//public class ReservationController {
//
//    private final PaymentService paymentService;
//    private ReservationController reservationService;
//
//    @PostMapping
//    public ResponseEntity<?> createReservation(
//            @RequestBody ReservationRequestDTO reservationDTO,
//            @RequestParam String impUid,
//            @RequestParam String merchantUid,
//            @RequestParam Long userId
//    ) throws Exception {
//        // 1. 예약 저장
//        Long reservationRequestId = reservationService.createReservation(reservationDTO, userId);
//
//        paymentService.processPayment(impUid, merchantUid, reservationRequestId, userId);
//        return ResponseEntity.ok("예약 및 결제 완료");
//    }
//
//}
