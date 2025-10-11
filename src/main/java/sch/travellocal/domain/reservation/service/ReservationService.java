package sch.travellocal.domain.reservation.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    public Long processReservation(ReservationRequestDTO dto, User currentUser) {

        // 프로그램 조회
        TourProgram tourProgram = tourProgramRepository.findById(dto.getTourProgramId())
                .orElseThrow(() -> new IllegalArgumentException("잘못된 프로그램 ID"));

        // 가이드를 DTO에서 안 받으면 프로그램 소유자를 기본 가이드로
        User guide = (dto.getGuideId() != null)
                ? userRepository.findById(dto.getGuideId())
                .orElseThrow(() -> new IllegalArgumentException("가이드가 존재하지 않습니다."))
                : tourProgram.getUser();

        ReservationRequest reservation = ReservationRequest.builder()
                .requestDate(LocalDateTime.now())
                .numOfPeople(dto.getNumOfPeople())
                .guideStartDate(dto.getGuideStartDate())
                .guideEndDate(dto.getGuideEndDate())
                .requestStatus(RequestStatus.PENDING)
                .totalPrice(dto.getTotalPrice())
                .tourProgram(tourProgram)
                .user(currentUser)
                .guide(tourProgram.getUser())
                .build();

        reservationRepository.save(reservation);
        return reservation.getId();
    }

//    // 유저가 본인의 예약 확인
//    public List<ReservationCalendarDTO> getMyReservations(LocalDateTime start, LocalDateTime end) {
//        User currentUser = securityUserService.getUserByJwt();
//        System.out.println("예약 조회 시작");
//
//        List<ReservationRequest> reservations = reservationRepository
//                .findByGuideAndGuideStartDateBetween(currentUser, start, end);
//
//        List<ReservationCalendarDTO> result = reservations.stream()
//                .map(r -> new ReservationCalendarDTO(
//                        r.getId(),
//                        r.getTourProgram().getTitle(),
//                        r.getGuideStartDate(),
//                        r.getGuideEndDate(),
//                        r.getNumOfPeople(),
//                        r.getRequestStatus().name()
//                ))
//                .collect(Collectors.toList());
//        System.out.println("예약 내역은요 " + result);
//
//        return result;
//    }


    // 가이드가 받은 예약 확인
    public List<ReservationCalendarDTO> getMyReservations(LocalDateTime start, LocalDateTime end) {
        User currentUser = securityUserService.getUserByJwt();

        // 내가 GUIDE인 예약 + 내가 USER인 예약 모두 조회
        List<ReservationRequest> reservations = reservationRepository
                .findByGuideIdOrUserIdAndGuideStartDateBetween(
                        currentUser.getId(),
                        currentUser.getId(),
                        start,
                        end
                );

        System.out.println("현재 사용자 ID: " + currentUser.getId() + " 예약 내역 조회 (엔티티)");
        reservations.forEach(r -> System.out.println(r));

        // DTO 변환
        List<ReservationCalendarDTO> dtos = reservations.stream()
                .map(r -> {
                    boolean isGuide = r.getGuide().getId().equals(currentUser.getId());

                    if (isGuide) {
                        // GUIDE 입장
                        return new ReservationCalendarDTO(
                                r.getId(),
                                r.getTourProgram().getTitle(),
                                r.getGuideStartDate(),
                                r.getGuideEndDate(),
                                r.getNumOfPeople(),
                                r.getRequestStatus().name(),
                                "GUIDE"
                        );
                    } else {
                        // USER 입장
                        return new ReservationCalendarDTO(
                                r.getId(),
                                r.getTourProgram().getTitle(),
                                r.getGuideStartDate(),
                                r.getGuideEndDate(),
                                r.getNumOfPeople(),
                                r.getRequestStatus().name(),
                                "USER"
                        );
                    }
                })
                .collect(Collectors.toList());

        System.out.println("DTO 변환 후 예약 내역:");
        dtos.forEach(System.out::println);

        return dtos;
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