package sch.travellocal.domain.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sch.travellocal.domain.payment.service.PaymentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/verify")
    public ResponseEntity<String> verifyAndSavePayment(
            @RequestParam String impUid,
            @RequestParam String merchantUid,
            @RequestParam Long reservationRequestId,
            @RequestParam Long userId
    ) {
        try {
            paymentService.processPayment(impUid, merchantUid, reservationRequestId, userId);
            return ResponseEntity.ok("결제 정보가 성공적으로 저장되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("결제 검증 실패: " + e.getMessage());
        }
    }
}
