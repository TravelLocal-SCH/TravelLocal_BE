package sch.travellocal.domain.place.enums.gugun;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sch.travellocal.domain.place.enums.LawAddressCode;

/**
 * 울산광역시의 구군 법정동 코드 ENUM
 */
@Getter
@RequiredArgsConstructor
public enum UlsanGuGunCode implements LawAddressCode {
    JUNG("110", "중구"),
    NAM("140", "남구"),
    DONG("170", "동구"),
    BUK("200", "북구"),
    ULJU("710", "울주군");

    private final String code;
    private final String name;

    /**
     * 코드 값으로 ENUM 상수를 찾기
     * @param code 법정동 코드
     * @return UlsanGuGunCode
     */
    public static UlsanGuGunCode fromCode(String code) {
        for (UlsanGuGunCode gu : values()) {
            if (gu.getCode().equals(code)) {
                return gu;
            }
        }
        throw new IllegalArgumentException("Invalid Ulsan GuGunCode: " + code);
    }
}