package sch.travellocal.domain.place.enums.gugun;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sch.travellocal.domain.place.enums.LawAddressCode;

/**
 * 대전광역시의 구군 법정동 코드 ENUM
 */
@Getter
@RequiredArgsConstructor
public enum DaejeonGuGunCode implements LawAddressCode {
    DONG("110", "동구"),
    JUNG("140", "중구"),
    SEO("170", "서구"),
    YUSEONG("200", "유성구"),
    DAEDEOK("230", "대덕구");

    private final String code;
    private final String name;

    /**
     * 코드 값으로 ENUM 상수를 찾기
     * @param code 법정동 코드
     * @return DaejeonGuGunCode
     */
    public static DaejeonGuGunCode fromCode(String code) {
        for (DaejeonGuGunCode gu : values()) {
            if (gu.getCode().equals(code)) {
                return gu;
            }
        }
        throw new IllegalArgumentException("Invalid Daejeon GuGunCode: " + code);
    }
}