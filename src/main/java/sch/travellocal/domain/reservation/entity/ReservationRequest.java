package sch.travellocal.domain.reservation.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import sch.travellocal.domain.payment.entity.PaymentEntity;
import sch.travellocal.domain.reservation.enums.RequestStatus;
import sch.travellocal.domain.tourprogram.entity.TourProgram;
import sch.travellocal.domain.user.entity.User;

import java.time.LocalDateTime;
@Entity
@Table(name = "reservation_request") // DB 테이블명 지정
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ReservationRequest {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 예약 요청 ID (자동 증가)

    @Column(name = "request_date", nullable = false)
    private LocalDateTime requestDate; // 예약 요청 날짜

    @Column(name = "num_of_people", nullable = false)
    private int numOfPeople; // 예약 인원 수

    @Column(name = "total_price", nullable = false)
    private int totalPrice; // 총 결제 금액

    @Column(name = "guide_start_date", nullable = false)
    private LocalDateTime guideStartDate; // 가이드 시작 시간

    @Column(name = "guide_end_date", nullable = false)
    private LocalDateTime guideEndDate; // 가이드 종료 시간


    @Enumerated(EnumType.STRING)
    @Column(name = "request_status", nullable = false)
    @ColumnDefault("'PENDING'")
    private RequestStatus requestStatus; // 요청 상태 (PENDING, APPROVED 등)

    @Column(name = "guide_response")
    private String guideResponse; // 가이드의 응답 메시지 (선택사항)

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 예약한 사용자 (회원)

    @ManyToOne
    @JoinColumn(name = "guide_id", nullable = true)
    private User guide; // 예약 요청을 받는 가이드

    @ManyToOne
    @JoinColumn(name = "tour_program_id", nullable = false)
    private TourProgram tourProgram; // 예약한 투어 프로그램

    @OneToOne(mappedBy = "reservationRequest", cascade = CascadeType.ALL)
    private PaymentEntity payment;

}
