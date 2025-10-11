package sch.travellocal.domain.reservation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ReservationRequestDTO {

    private int numOfPeople; // 예약 인원 수

    private LocalDateTime guideStartDate; // 가이드 시작 날짜 및 시간

    private LocalDateTime guideEndDate; // 가이드 종료 날짜 및 시간

    @NotNull(message = "투어 프로그램 ID는 필수 값입니다.")
    private Long tourProgramId;

    private String paymentMethod; // 결제 수단 (ex: 카드, 카카오페이 등)

    private Long guideId;

    private int totalPrice; // 총 결제 금액


}