package sch.travellocal.domain.payment.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sch.travellocal.domain.payment.entity.PaymentEntity;
import sch.travellocal.domain.payment.enums.PaymentStatus;
import sch.travellocal.domain.payment.repository.PaymentRepository;
import sch.travellocal.domain.reservation.entity.ReservationRequest;
import sch.travellocal.domain.reservation.repository.ReservationRequestRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Service
public class PaymentService {

    private final IamportClient iamportClient;
    private final PaymentRepository paymentRepository;
    private final ReservationRequestRepository reservationRequestRepository;

    public PaymentService(@Value("${iamport.api-key}") String apiKey,
                          @Value("${iamport.api-secret}") String apiSecret,
                          PaymentRepository paymentRepository,
                          ReservationRequestRepository reservationRequestRepository) {
        this.iamportClient = new IamportClient(apiKey, apiSecret);
        this.paymentRepository = paymentRepository;
        this.reservationRequestRepository = reservationRequestRepository;
    }

    @Transactional
    public void processPayment(String impUid, String merchantUid, Long reservationRequestId, Long id) {
        // 1. 이미 결제된 예약인지 체크
        if (paymentRepository.existsByReservationRequestId(reservationRequestId)) {
            log.info("이미 결제된 예약입니다. reservationRequestId={}", reservationRequestId);
            return;
        }

        // 2. 아임포트 결제 조회
        Payment iamportPayment;
        try {
            IamportResponse<Payment> response = iamportClient.paymentByImpUid(impUid);
            iamportPayment = response.getResponse();
            if (iamportPayment == null || !"paid".equals(iamportPayment.getStatus())) {
                refund(impUid, "결제가 완료되지 않았습니다.");
            }
        } catch (Exception e) {
            log.error("아임포트 결제 조회 실패: {}", e.getMessage());
            throw new IllegalStateException("결제 조회 중 오류 발생");
        }

        // 3. 예약 정보 조회
        ReservationRequest reservationRequest = reservationRequestRepository.findById(reservationRequestId)
                .orElseThrow(() -> {
                    refund(impUid, "예약 정보가 존재하지 않습니다.");
                    return new IllegalStateException(); // 실제로는 refund에서 예외 발생
                });

        // 4. 금액 검증
        if (reservationRequest.getTotalPrice() != iamportPayment.getAmount().intValue()) {
            refund(impUid, "결제 금액 불일치");
        }

        // 5. 결제 엔티티 생성 후 저장
        PaymentEntity payment = PaymentEntity.builder()
                .impUid(impUid)
                .paymentMethod(iamportPayment.getPayMethod())
                .paymentStatus(PaymentStatus.PAID)
                .totalPrice(iamportPayment.getAmount().intValue())
                .paidAt(LocalDateTime.ofInstant(iamportPayment.getPaidAt().toInstant(), ZoneId.systemDefault()))
                .reservationRequest(reservationRequest)
                .user(reservationRequest.getUser())
                .build();

        paymentRepository.save(payment);
        log.info("결제 저장 완료: reservationRequestId={}, impUid={}", reservationRequestId, impUid);
    }

    private void refund(String impUid, String reason) {
        try {
            iamportClient.cancelPaymentByImpUid(new CancelData(impUid, true));
            log.warn("결제 환불 처리됨. impUid={}, reason={}", impUid, reason);
        } catch (Exception e) {
            log.error("환불 처리 실패: impUid={}, error={}", impUid, e.getMessage());
        }
        throw new IllegalStateException(reason);
    }
}
