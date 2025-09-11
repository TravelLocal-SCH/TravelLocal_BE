package sch.travellocal.domain.place.enums.gugun;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sch.travellocal.domain.place.enums.LawAddressCode;

/**
 * 대구광역시의 구군 법정동 코드 ENUM
 */
@Getter
@RequiredArgsConstructor
public enum DaeguGuGunCode implements LawAddressCode {
    JUNG("110", "중구"),
    DONG("140", "동구"),
    SEO("170", "서구"),
    NAM("200", "남구"),
    BUK("230", "북구"),
    SUSEONG("260", "수성구"),
    DALSEO("290", "달서구"),
    DALSEONG("710", "달성군"),
    GUNWI("720", "군위군");

    private final String code;
    private final String name;

    /**
     * 코드 값으로 ENUM 찾기
     */
    public static DaeguGuGunCode fromCode(String code) {
        for (DaeguGuGunCode gu : values()) {
            if (gu.getCode().equals(code)) {
                return gu;
            }
        }
        throw new IllegalArgumentException("Invalid Daegu GuGunCode: " + code);
    }
}
