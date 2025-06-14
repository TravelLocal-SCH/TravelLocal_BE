package sch.travellocal.domain.payment.service;

import com.siot.IamportRestClient.IamportClient;
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


    // 프론트에서 받은 결제 정보를 토대로 아임포트에 검증 요청을 하는 서비스 로직
    public void processPayment(String impUid, String merchantUid, Long reservationRequestId, Long userId) throws Exception {
        // 결제 내역 중복 확인
        if (paymentRepository.existsByImpUid(impUid)) {
            log.info("이미 처리된 결제입니다: {}", impUid);
            return; // 예외 대신 조용히 종료하거나 필요시 기존 결제 반환 가능
        }

        IamportResponse<Payment> response = iamportClient.paymentByImpUid(impUid);

        if (response.getResponse() == null || !"paid".equals(response.getResponse().getStatus())) {
            throw new IllegalStateException("결제가 완료되지 않았습니다.");
        }

        Payment iamportPayment = response.getResponse();

        ReservationRequest reservationRequest = reservationRequestRepository.findById(reservationRequestId)
                .orElseThrow(() -> new IllegalArgumentException("해당 예약 요청이 존재하지 않습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        int expectedPrice = reservationRequest.getTotalPrice();
        System.out.println("expectedPrice : " + expectedPrice);
        int paidPrice = iamportPayment.getAmount().intValue();
        System.out.println("paidPrice : " + paidPrice);

        if (expectedPrice != paidPrice) {
            throw new IllegalStateException("결제 금액이 일치하지 않습니다.");
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

}
