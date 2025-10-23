package sch.travellocal.domain.reservation.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReservationAndPaymentRequestDTO {
    private ReservationRequestDTO reservation;
    private String impUid;
    private String merchantUid;
}
