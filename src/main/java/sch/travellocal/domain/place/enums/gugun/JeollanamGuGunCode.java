package sch.travellocal.domain.place.enums.gugun;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sch.travellocal.domain.place.enums.LawAddressCode;

/**
 * 전라남도 시군구 법정동 코드 ENUM
 */
@Getter
@RequiredArgsConstructor
public enum JeollanamGuGunCode implements LawAddressCode {
    MOKPO("110", "목포시"),
    YEOSU("130", "여수시"),
    SUNCHEON("150", "순천시"),
    NAJU("170", "나주시"),
    GWANGYANG("230", "광양시"),

    DAMYANG("710", "담양군"),
    GOKSEONG("720", "곡성군"),
    GURYE("730", "구례군"),
    GOHEUNG("770", "고흥군"),
    BOSEONG("780", "보성군"),
    HWASUN("790", "화순군"),
    JANGHEUNG("800", "장흥군"),
    GANGJIN("810", "강진군"),
    HAENAM("820", "해남군"),
    YEONGAM("830", "영암군"),
    MUAN("840", "무안군"),
    HAMPYEONG("860", "함평군"),
    YEONGGWANG("870", "영광군"),
    JANGSEONG("880", "장성군"),
    WANDO("890", "완도군"),
    JINDO("900", "진도군"),
    SINAN("910", "신안군");

    private final String code;
    private final String name;

    /**
     * 코드 값으로 ENUM 상수를 찾기
     * @param code 법정동 코드
     * @return JeollanamGuGunCode
     */
    public static JeollanamGuGunCode fromCode(String code) {
        for (JeollanamGuGunCode gu : values()) {
            if (gu.getCode().equals(code)) {
                return gu;
            }
        }
        throw new IllegalArgumentException("Invalid Jeollanam GuGunCode: " + code);
    }
}