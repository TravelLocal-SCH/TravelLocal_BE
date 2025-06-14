package sch.travellocal.domain.payment.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {
    private String impUid;
    private String paymentMethod;
    private int totalPrice;
}
