package sch.travellocal.domain.place.enums.gugun;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sch.travellocal.domain.place.enums.LawAddressCode;

/**
 * 제주특별자치도 시군구 법정동 코드 ENUM
 */
@Getter
@RequiredArgsConstructor
public enum JejuGuGunCode implements LawAddressCode {
    JEJU("110", "제주시"),
    SEOGWIPO("130", "서귀포시");

    private final String code;
    private final String name;

    /**
     * 코드 값으로 ENUM 상수를 찾기
     * @param code 법정동 코드
     * @return JejuGuGunCode
     */
    public static JejuGuGunCode fromCode(String code) {
        for (JejuGuGunCode gu : values()) {
            if (gu.getCode().equals(code)) {
                return gu;
            }
        }
        throw new IllegalArgumentException("Invalid Jeju GuGunCode: " + code);
    }
}