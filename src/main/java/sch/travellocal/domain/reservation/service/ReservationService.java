package sch.travellocal.domain.reservation.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sch.travellocal.domain.payment.entity.Payment;
import sch.travellocal.domain.payment.enums.PaymentStatus;
import sch.travellocal.domain.payment.repository.PaymentRepository;
import sch.travellocal.domain.reservation.dto.ReservationRequestDTO;
import sch.travellocal.domain.reservation.entity.ReservationRequest;
import sch.travellocal.domain.reservation.repository.ReservationRequestRepository;
import sch.travellocal.domain.reservation.enums.RequestStatus;
import sch.travellocal.domain.tourprogram.entity.TourProgram;
import sch.travellocal.domain.tourprogram.repository.TourProgramRepository;
import sch.travellocal.domain.user.entity.User;
import sch.travellocal.domain.user.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRequestRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    private final TourProgramRepository tourProgramRepository; //경탁이 코드에서 프로그램에 대한 레포지토리 필요함
    private final UserRepository userRepository;

    @Transactional
    public void processReservation(ReservationRequestDTO dto, String impUid) {
        // 예시용 유저 (실제로는 로그인한 유저의 정보를 받아와야 함)
        User user = userRepository.findById(1L).orElseThrow();

        // 예약하려는 투어 프로그램을 DB에서 조회
        TourProgram tourProgram = tourProgramRepository.findById(dto.getTourProgramId())
                .orElseThrow(() -> new IllegalArgumentException("잘못된 프로그램 ID"));

        // 예약 요청 엔티티 생성
        ReservationRequest reservation = ReservationRequest.builder()
                .requestDate(LocalDateTime.now()) // 현재 시간 기준으로 요청 시간 저장
                .numOfPeople(dto.getNumOfPeople()) // 인원 수 설정
                .guideStartDate(dto.getGuideStartDate()) // 가이드 시작 날짜
                .guideEndDate(dto.getGuideEndDate()) // 가이드 종료 날짜
                .requestStatus(RequestStatus.PENDING) // 초기 상태는 PENDING
                .tourProgram(tourProgram) // 예약한 투어 프로그램 연결
                .user(user) // 예약한 사용자 정보 연결
                .build();

        // 예약 정보 저장
        reservationRepository.save(reservation);

        // 결제 정보 생성
        Payment payment = Payment.builder()
                .totalPrice(dto.getTotalPrice()) // 총 결제 금액
                .paymentMethod(dto.getPaymentMethod()) // 결제 수단
                .impUid(impUid) // 아임포트에서 받은 결제 고유 ID
                .paymentStatus(PaymentStatus.PAID) // 결제 상태 (여기서는 단순히 PAID로 처리)
                .paidAt(LocalDateTime.now()) // 결제 완료 시간
                .reservationRequest(reservation) // 결제와 예약 연결
                .user(user) // 결제한 사용자
                .build();

        // 결제 정보 저장
        paymentRepository.save(payment);

        // 양방향 관계를 위해 예약에 결제 정보 연결 (양방향 매핑 시 필요)
        reservation.setPayment(payment);
    }

}
