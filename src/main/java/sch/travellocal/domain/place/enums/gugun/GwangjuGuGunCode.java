package sch.travellocal.domain.place.enums.gugun;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sch.travellocal.domain.place.enums.LawAddressCode;

/**
 * 광주광역시의 구군 법정동 코드 ENUM
 */
@Getter
@RequiredArgsConstructor
public enum GwangjuGuGunCode implements LawAddressCode {
    DONG("110", "동구"),
    SEO("140", "서구"),
    NAM("155", "남구"),
    BUK("170", "북구"),
    GWANGSAN("200", "광산구");

    private final String code;
    private final String name;

    /**
     * 코드 값으로 ENUM 상수를 찾기
     * @param code 법정동 코드
     * @return GwangjuGuGunCode
     */
    public static GwangjuGuGunCode fromCode(String code) {
        for (GwangjuGuGunCode gu : values()) {
            if (gu.getCode().equals(code)) {
                return gu;
            }
        }
        throw new IllegalArgumentException("Invalid Gwangju GuGunCode: " + code);
    }
}