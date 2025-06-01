package sch.travellocal.domain.reservation.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sch.travellocal.domain.payment.entity.PaymentEntity;
import sch.travellocal.domain.payment.enums.PaymentStatus;
import sch.travellocal.domain.payment.repository.PaymentRepository;
import sch.travellocal.domain.reservation.dto.ReservationCalendarDTO;
import sch.travellocal.domain.reservation.dto.ReservationRequestDTO;
import sch.travellocal.domain.reservation.entity.ReservationRequest;
import sch.travellocal.domain.reservation.entity.ReservationRequestCalendarResponse;
import sch.travellocal.domain.reservation.repository.ReservationRequestRepository;
import sch.travellocal.domain.reservation.enums.RequestStatus;
import sch.travellocal.domain.tourprogram.entity.TourProgram;
import sch.travellocal.domain.tourprogram.repository.TourProgramRepository;
import sch.travellocal.domain.user.entity.User;
import sch.travellocal.domain.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRequestRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    private final TourProgramRepository tourProgramRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long processReservation(ReservationRequestDTO dto, String impUid) {
        User user = userRepository.findById(1L).orElseThrow();

        TourProgram tourProgram = tourProgramRepository.findById(dto.getTourProgramId())
                .orElseThrow(() -> new IllegalArgumentException("잘못된 프로그램 ID"));

        ReservationRequest reservation = ReservationRequest.builder()
                .requestDate(LocalDateTime.now())
                .numOfPeople(dto.getNumOfPeople())
                .guideStartDate(dto.getGuideStartDate())
                .guideEndDate(dto.getGuideEndDate())
                .requestStatus(RequestStatus.PENDING)
                .tourProgram(tourProgram)
                .user(user)
                .build();

        reservationRepository.save(reservation);

        PaymentEntity payment = PaymentEntity.builder()
                .totalPrice(dto.getTotalPrice())
                .paymentMethod(dto.getPaymentMethod())
                .impUid(impUid)
                .paymentStatus(PaymentStatus.PAID)
                .paidAt(LocalDateTime.now())
                .reservationRequest(reservation)
                .user(user)
                .build();

        paymentRepository.save(payment);

        reservation.setPayment(payment);

        return reservation.getId();
    }

    public List<ReservationRequestCalendarResponse> getReservationsBetween(LocalDateTime start, LocalDateTime end) {
        List<ReservationRequest> reservations = reservationRepository.findByGuideStartDateBetween(start, end);
        return reservations.stream()
                .map(ReservationRequestCalendarResponse::from)
                .collect(Collectors.toList());
    }

    // 수정된 부분: 반환 타입과 DTO 맞춤
    public List<ReservationCalendarDTO> getReservationsForCalendar(LocalDateTime start, LocalDateTime end) {
        List<ReservationRequest> reservations = reservationRepository.findByGuideStartDateBetween(start, end);

        return reservations.stream()
                .map(r -> new ReservationCalendarDTO(
                        r.getTourProgram().getTitle(),
                        r.getGuideStartDate(),
                        r.getGuideEndDate(),
                        r.getUser().getName()
                ))
                .collect(Collectors.toList());
    }
}
