package sch.travellocal.domain.reservation.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationAndPaymentRequestDTO {
    private ReservationRequestDTO reservation;
    private String impUid;
    private String merchantUid;
    private Long userId;
}
