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
            System.out.println("✅ POST /api/reservations 호출됨");
            System.out.println("받은 값: " + request);

            // 먼저 JWT에서 로그인한 사용자 정보 가져오기
            User currentUser = securityUserService.getUserByJwt();
            System.out.println("👤 JWT 유저 정보 확인 - userId: " + currentUser.getId() + ", username: " + currentUser.getUsername());

            ReservationRequestDTO reservationDTO = request.getReservation();
            System.out.println("📦 예약 DTO 확인: " + reservationDTO.toString());

//            String impUid = request.getImpUid();
//            String merchantUid = request.getMerchantUid();
//            System.out.println("💳 결제 정보 확인 - impUid: " + impUid + ", merchantUid: " + merchantUid);

            // 예약 처리 (currentUser 전달)
            Long reservationRequestId = reservationService.processReservation(reservationDTO, currentUser);
            System.out.println("📝 예약 처리 완료 - reservationRequestId: " + reservationRequestId);

//            // 결제 처리
//            paymentService.processPayment(impUid, merchantUid, reservationRequestId, currentUser.getId());
//            System.out.println("💰 결제 처리 완료");

//            return ResponseEntity.ok("예약 및 결제 완료");
            return ResponseEntity.ok("예약 완료");
        } catch (Exception e) {
            System.out.println("❌ 예외 발생: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("예약 또는 결제 실패: " + e.getMessage());
        }
    }


}
