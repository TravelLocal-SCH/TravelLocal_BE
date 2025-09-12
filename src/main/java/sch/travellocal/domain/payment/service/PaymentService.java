package sch.travellocal.domain.payment.service;


import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sch.travellocal.domain.payment.entity.PaymentEntity;
import sch.travellocal.domain.payment.enums.PaymentStatus;
import sch.travellocal.domain.payment.repository.PaymentRepository;
import sch.travellocal.domain.reservation.entity.ReservationRequest;
import sch.travellocal.domain.reservation.repository.ReservationRequestRepository;
import sch.travellocal.domain.user.entity.User;
import sch.travellocal.domain.user.repository.UserRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Service
public class PaymentService {

    private final IamportClient iamportClient;
    private final PaymentRepository paymentRepository;
    private final ReservationRequestRepository reservationRequestRepository;
    private final UserRepository userRepository;

    public PaymentService(@Value("${iamport.api-key}") String apiKey,
                          @Value("${iamport.api-secret}") String apiSecret,
                          PaymentRepository paymentRepository,
                          ReservationRequestRepository reservationRequestRepository,
                          UserRepository userRepository) {
        this.iamportClient = new IamportClient(apiKey, apiSecret);
        this.paymentRepository = paymentRepository;
        this.reservationRequestRepository = reservationRequestRepository;
        this.userRepository = userRepository;
    }


    //결제 검증 로직(에러 발생 시 환불 조치)
    public void processPayment(String impUid, String merchantUid, Long reservationRequestId, Long userId) throws Exception {
        if (paymentRepository.existsByImpUid(impUid)) {
            log.info("이미 처리된 결제입니다: {}", impUid);
            return;
        }

        IamportResponse<Payment> response;
        try {
            response = iamportClient.paymentByImpUid(impUid);
        } catch (Exception e) {
            log.error("아임포트 결제 조회 실패: {}", e.getMessage());
            throw new IllegalStateException("결제 조회 중 오류가 발생했습니다.");
        }

        if (response.getResponse() == null || !"paid".equals(response.getResponse().getStatus())) {
            refund(impUid, "결제가 완료되지 않았습니다.");
        }

        Payment iamportPayment = response.getResponse();

        ReservationRequest reservationRequest = reservationRequestRepository.findById(reservationRequestId)
                .orElse(null);
        if (reservationRequest == null) {
            refund(impUid, "해당 예약 요청이 존재하지 않습니다.");
        }

        // reservationRequest에 연결된 유저를 사용
        User user = reservationRequest.getUser();
        if (user == null) {
            refund(impUid, "예약자 정보가 존재하지 않습니다.");
        }


        int expectedPrice = reservationRequest.getTotalPrice();
        int paidPrice = iamportPayment.getAmount().intValue();

        if (expectedPrice != paidPrice) {
            refund(impUid, "결제 금액이 일치하지 않습니다.");
        }

        PaymentEntity payment = PaymentEntity.builder()
                .impUid(impUid)
                .paymentMethod(iamportPayment.getPayMethod())
                .paymentStatus(PaymentStatus.PAID)
                .totalPrice(paidPrice)
                .paidAt(LocalDateTime.ofInstant(iamportPayment.getPaidAt().toInstant(), ZoneId.systemDefault()))
                .reservationRequest(reservationRequest)
                .user(user)
                .build();

        paymentRepository.save(payment);
    }

    private void refund(String impUid, String reason) {
        try {
            CancelData cancelData = new CancelData(impUid, true);
            iamportClient.cancelPaymentByImpUid(cancelData);
            log.warn("결제 환불 처리됨. impUid={}, reason={}", impUid, reason);
        } catch (Exception e) {
            log.error("환불 처리 중 오류 발생: impUid={}, error={}", impUid, e.getMessage());
        }

        throw new IllegalStateException(reason);
    }
}