package sch.travellocal.domain.place.enums.gugun;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sch.travellocal.domain.place.enums.LawAddressCode;

/**
 * 인천광역시의 구군 법정동 코드 ENUM
 */
@Getter
@RequiredArgsConstructor
public enum IncheonGuGunCode implements LawAddressCode {
    JUNG("110", "중구"),
    DONG("140", "동구"),
    MICHUHOL("177", "미추홀구"),
    YEONSU("185", "연수구"),
    NAMDONG("200", "남동구"),
    BUPYEONG("237", "부평구"),
    GYEOYANG("245", "계양구"),
    SEO("260", "서구"),
    GANGHWA("710", "강화군"),
    ONGJIN("720", "옹진군");

    private final String code;
    private final String name;

    /**
     * 코드 값으로 ENUM 찾기
     */
    public static IncheonGuGunCode fromCode(String code) {
        for (IncheonGuGunCode gu : values()) {
            if (gu.getCode().equals(code)) {
                return gu;
            }
        }
        throw new IllegalArgumentException("Invalid IncheonGuGunCode: " + code);
    }
}