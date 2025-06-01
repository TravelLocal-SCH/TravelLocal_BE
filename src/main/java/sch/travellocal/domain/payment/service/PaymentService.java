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

    public void processPayment(String impUid, String merchantUid, Long reservationRequestId, Long userId) throws Exception {
        IamportResponse<Payment> response = iamportClient.paymentByImpUid(impUid);

        if (response.getResponse() == null || !"paid".equals(response.getResponse().getStatus())) {
            throw new IllegalStateException("결제가 완료되지 않았습니다.");
        }

        Payment iamportPayment = response.getResponse();

        ReservationRequest reservationRequest = reservationRequestRepository.findById(reservationRequestId)
                .orElseThrow(() -> new IllegalArgumentException("해당 예약 요청이 존재하지 않습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        // 💡 금액 검증
        int expectedPrice = reservationRequest.getTotalPrice(); // 예약 시 금액
        int paidPrice = iamportPayment.getAmount().intValue();

        if (expectedPrice != paidPrice) {
            throw new IllegalStateException("결제 금액이 일치하지 않습니다.");
        }

        // 💾 DB 저장
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
