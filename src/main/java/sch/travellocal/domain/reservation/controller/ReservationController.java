package sch.travellocal.domain.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sch.travellocal.auth.oauth.CustomOAuth2User;
import sch.travellocal.domain.payment.service.PaymentService;
import sch.travellocal.domain.reservation.dto.ReservationAndPaymentRequestDTO;
import sch.travellocal.domain.reservation.dto.ReservationRequestDTO;
import sch.travellocal.domain.reservation.enums.RequestStatus;
import sch.travellocal.domain.reservation.service.ReservationService;
import sch.travellocal.domain.user.entity.User;
import sch.travellocal.domain.user.service.SecurityUserService;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final PaymentService paymentService;
    private final SecurityUserService securityUserService;


    //예약 상태 변경 API
    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateReservationStatus(
            @PathVariable("id") Long reservationId,
            @RequestParam("status") RequestStatus status) {

        reservationService.updateReservationStatus(reservationId, status);
        return ResponseEntity.ok("예약 상태가 '" + status.name() + "'(으)로 변경되었습니다.");
    }


    // 프론트에서 넘어온
    @PostMapping
    public ResponseEntity<String> createReservationAndPayment(
            @RequestBody ReservationAndPaymentRequestDTO request
    ) {
        try {

            // 먼저 JWT에서 로그인한 사용자 정보 가져오기
            User currentUser = securityUserService.getUserByJwt();

            ReservationRequestDTO reservationDTO = request.getReservation();

//            String impUid = request.getImpUid();
//            String merchantUid = request.getMerchantUid();

            // 예약 처리 (currentUser 전달)
            Long reservationRequestId = reservationService.processReservation(reservationDTO, currentUser);

//            // 결제 처리
//            paymentService.processPayment(impUid, merchantUid, reservationRequestId, currentUser.getId());

//            return ResponseEntity.ok("예약 및 결제 완료");
            return ResponseEntity.ok("예약 완료");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("예약 또는 결제 실패: " + e.getMessage());
        }
    }
}
