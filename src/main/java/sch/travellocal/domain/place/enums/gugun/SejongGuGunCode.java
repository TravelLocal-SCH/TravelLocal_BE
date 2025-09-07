package sch.travellocal.domain.place.enums.gugun;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sch.travellocal.domain.place.enums.LawAddressCode;

/**
 * 세종특별자치시 법정동 코드 ENUM
 */
@Getter
@RequiredArgsConstructor
public enum SejongGuGunCode implements LawAddressCode {
    SEJONG("36110", "세종특별자치시");

    private final String code;
    private final String name;

    /**
     * 코드 값으로 ENUM 상수를 찾기
     * @param code 법정동 코드
     * @return SejongGuGunCode
     */
    public static SejongGuGunCode fromCode(String code) {
        for (SejongGuGunCode gu : values()) {
            if (gu.getCode().equals(code)) {
                return gu;
            }
        }
        throw new IllegalArgumentException("Invalid Sejong GuGunCode: " + code);
    }
}