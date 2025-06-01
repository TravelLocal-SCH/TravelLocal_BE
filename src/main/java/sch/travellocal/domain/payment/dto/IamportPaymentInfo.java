package sch.travellocal.domain.payment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IamportPaymentInfo {

    private String imp_uid;
    private String merchant_uid;
    private String pay_method;
    private String status;       // paid, cancelled 등
    private int amount;          // 실제 결제된 금액
    private String name;         // 상품 이름
    private String buyer_email;
    private String buyer_name;
    private String buyer_tel;
    private String buyer_addr;
    private String buyer_postcode;
    private long paid_at;        // 유닉스 타임스탬프
}