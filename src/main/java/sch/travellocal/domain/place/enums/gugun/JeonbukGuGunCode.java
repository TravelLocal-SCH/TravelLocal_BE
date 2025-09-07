package sch.travellocal.domain.place.enums.gugun;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sch.travellocal.domain.place.enums.LawAddressCode;

/**
 * 전북특별자치도 시군구 법정동 코드 ENUM
 */
@Getter
@RequiredArgsConstructor
public enum JeonbukGuGunCode implements LawAddressCode {
    JEONJU("110", "전주시"),
    JEONJU_WANSAN("111", "전주시 완산구"),
    JEONJU_DEOKJIN("113", "전주시 덕진구"),
    GUNSAN("130", "군산시"),
    IKSAN("140", "익산시"),
    JEONGEUP("180", "정읍시"),
    NAMWON("190", "남원시"),
    GIMJE("210", "김제시"),
    WANJU("710", "완주군"),
    JINAN("720", "진안군"),
    MUJU("730", "무주군"),
    JANGSU("740", "장수군"),
    IMSIL("750", "임실군"),
    SOONCHANG("770", "순창군"),
    GOCHANG("790", "고창군"),
    BUAN("800", "부안군");

    private final String code;
    private final String name;

    /**
     * 코드 값으로 ENUM 상수를 찾기
     * @param code 법정동 코드
     * @return JeonbukGuGunCode
     */
    public static JeonbukGuGunCode fromCode(String code) {
        for (JeonbukGuGunCode gu : values()) {
            if (gu.getCode().equals(code)) {
                return gu;
            }
        }
        throw new IllegalArgumentException("Invalid Jeonbuk GuGunCode: " + code);
    }
}