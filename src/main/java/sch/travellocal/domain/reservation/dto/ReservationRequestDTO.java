package sch.travellocal.domain.reservation.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequestDTO {

    private int numOfPeople; // 예약 인원 수

    private LocalDateTime guideStartDate; // 가이드 시작 날짜 및 시간

    private LocalDateTime guideEndDate; // 가이드 종료 날짜 및 시간

    private Long tourProgramId; // 예약할 투어 프로그램의 ID

    private String paymentMethod; // 결제 수단 (ex: 카드, 카카오페이 등)

    private int totalPrice; // 총 결제 금액
}