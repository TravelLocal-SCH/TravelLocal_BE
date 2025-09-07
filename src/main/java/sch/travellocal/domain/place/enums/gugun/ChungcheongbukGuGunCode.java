package sch.travellocal.domain.place.enums.gugun;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sch.travellocal.domain.place.enums.LawAddressCode;

/**
 * 충청북도 시군구 법정동 코드 ENUM
 */
@Getter
@RequiredArgsConstructor
public enum ChungcheongbukGuGunCode implements LawAddressCode {
    CHEONGJU("110", "청주시"),
    CHEONGJU_SANGDANG("111", "청주시 상당구"),
    CHEONGJU_SEOWON("112", "청주시 서원구"),
    CHEONGJU_HEUNGDEOK("113", "청주시 흥덕구"),
    CHEONGJU_CHEONGWON("114", "청주시 청원구"),

    CHUNGJU("130", "충주시"),
    JECHEON("150", "제천시"),

    BOEUN("720", "보은군"),
    OKCHEON("730", "옥천군"),
    YEONGDONG("740", "영동군"),
    JUNGPEONG("745", "증평군"),
    JINCHEON("750", "진천군"),
    GOESAN("760", "괴산군"),
    EUMSEONG("770", "음성군"),
    DANYANG("800", "단양군");

    private final String code;
    private final String name;

    /**
     * 코드 값으로 ENUM 상수를 찾기
     * @param code 법정동 코드
     * @return ChungcheongbukGuGunCode
     */
    public static ChungcheongbukGuGunCode fromCode(String code) {
        for (ChungcheongbukGuGunCode gu : values()) {
            if (gu.getCode().equals(code)) {
                return gu;
            }
        }
        throw new IllegalArgumentException("Invalid Chungcheongbuk GuGunCode: " + code);
    }
}