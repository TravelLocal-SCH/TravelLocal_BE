package sch.travellocal.domain.reservation.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sch.travellocal.domain.payment.entity.PaymentEntity;
import sch.travellocal.domain.payment.enums.PaymentStatus;
import sch.travellocal.domain.payment.repository.PaymentRepository;
import sch.travellocal.domain.reservation.dto.CalendarStatusDTO;
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
import sch.travellocal.domain.user.service.SecurityUserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRequestRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    private final TourProgramRepository tourProgramRepository;
    private final UserRepository userRepository;
    private final SecurityUserService securityUserService;

    @Transactional
    public Long processReservation(ReservationRequestDTO dto) {
        User user = userRepository.findById(1L).orElseThrow();

        TourProgram tourProgram = tourProgramRepository.findById(dto.getTourProgramId())
                .orElseThrow(() -> new IllegalArgumentException("잘못된 프로그램 ID"));

        User guide = null;
        if (dto.getGuideId() != null) {
            guide = userRepository.findById(dto.getGuideId()).orElse(null);
        }

        ReservationRequest reservation = ReservationRequest.builder()
                .requestDate(LocalDateTime.now())
                .numOfPeople(dto.getNumOfPeople())
                .guideStartDate(dto.getGuideStartDate())
                .guideEndDate(dto.getGuideEndDate())
                .requestStatus(RequestStatus.PENDING)
                .totalPrice(dto.getTotalPrice())
                .tourProgram(tourProgram)
                .user(user)
                .guide(guide)
                .build();

        reservationRepository.save(reservation);
        return reservation.getId();
    }

    // 유저가 본인의 예약 확인
    public List<ReservationCalendarDTO> getMyReservations(LocalDateTime start, LocalDateTime end) {
        User currentUser = securityUserService.getUserByJwt();

        List<ReservationRequest> reservations = reservationRepository
                .findByGuideAndGuideStartDateBetween(currentUser, start, end);

        return reservations.stream()
                .map(r -> new ReservationCalendarDTO(
                        r.getId(),
                        r.getTourProgram().getTitle(),
                        r.getGuideStartDate(),
                        r.getGuideEndDate(),
                        r.getNumOfPeople(),
                        r.getRequestStatus().name() // enum -> 문자열
                ))
                .collect(Collectors.toList());
    }

    // 가이드가 받은 예약 확인
    public List<ReservationCalendarDTO> getReceivedReservationsAsGuide(LocalDateTime start, LocalDateTime end) {
        User currentUser = securityUserService.getUserByJwt();

        List<ReservationRequest> reservations = reservationRepository
                .findByGuideAndGuideStartDateBetween(currentUser, start, end);

        return reservations.stream()
                .map(r -> new ReservationCalendarDTO(
                        r.getId(),
                        r.getTourProgram().getTitle(),
                        r.getGuideStartDate(),
                        r.getGuideEndDate(),
                        r.getNumOfPeople(),
                        r.getRequestStatus().name()
                ))
                .collect(Collectors.toList());
    }


    //캘린더 들어갔을 때 예약 날짜만 색깔로 표시
    public List<CalendarStatusDTO> getReservationDatesWithStatusForUser(LocalDate start, LocalDate end) {
        try {
            User currentUser = securityUserService.getUserByJwt();
            List<ReservationRequest> reservations = reservationRepository
                    .findByGuideAndGuideStartDateBetween(currentUser, start.atStartOfDay(), end.atTime(23, 59));

            List<CalendarStatusDTO> result = new ArrayList<>();

            for (ReservationRequest res : reservations) {
                LocalDate resStart = res.getGuideStartDate().toLocalDate();
                LocalDate resEnd = res.getGuideEndDate().toLocalDate();

                for (LocalDate date = resStart; !date.isAfter(resEnd); date = date.plusDays(1)) {
                    if (!date.isBefore(start) && !date.isAfter(end)) {
                        result.add(new CalendarStatusDTO(
                                res.getId(),
                                date,
                                res.getRequestStatus().name()
                        ));
                    }
                }
            }

            return result;
        } catch (Exception e) {
            System.out.println("🔥 예약 상태 조회 중 예외 발생: " + e.getMessage());
            e.printStackTrace();
            throw e;  // 또는 적절한 커스텀 예외 래핑
        }
    }

    // 예약 취소시 삭제 코드
    @Transactional
    public void updateReservationStatus(Long reservationId, RequestStatus newStatus) {
        ReservationRequest reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 예약이 존재하지 않습니다."));

        // (선택) 상태가 같으면 무시
        if (reservation.getRequestStatus() == newStatus) {
            return;
        }

        // (선택) 완료된 예약은 수정 불가
        if (reservation.getRequestStatus() == RequestStatus.COMPLETED) {
            throw new IllegalStateException("완료된 예약은 상태를 변경할 수 없습니다.");
        }

        reservation.setRequestStatus(newStatus);
    }
}