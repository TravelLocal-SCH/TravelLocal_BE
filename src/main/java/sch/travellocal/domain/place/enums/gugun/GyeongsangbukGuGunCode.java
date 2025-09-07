package sch.travellocal.domain.place.enums.gugun;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sch.travellocal.domain.place.enums.LawAddressCode;

/**
 * 경상북도 시군구 법정동 코드 ENUM
 */
@Getter
@RequiredArgsConstructor
public enum GyeongsangbukGuGunCode implements LawAddressCode {
    POHANG("110", "포항시"),
    POHANG_NAMGU("111", "포항시 남구"),
    POHANG_BUKGU("113", "포항시 북구"),
    GYEONGJU("130", "경주시"),
    GIMCHEON("150", "김천시"),
    ANDONG("170", "안동시"),
    GUMI("190", "구미시"),
    YEONGJU("210", "영주시"),
    YEONGCHEON("230", "영천시"),
    SANGJU("250", "상주시"),
    MUNGYEONG("280", "문경시"),
    GYEONGSAN("290", "경산시"),

    UISEONG("730", "의성군"),
    CHEONGSONG("750", "청송군"),
    YEONGYANG("760", "영양군"),
    YEONGDEOK("770", "영덕군"),
    CHEONGDO("820", "청도군"),
    GOERYEONG("830", "고령군"),
    SEONGJU("840", "성주군"),
    CHILGOK("850", "칠곡군"),
    YECHEON("900", "예천군"),
    BONGHWA("920", "봉화군"),
    ULJIN("930", "울진군"),
    ULLUNG("940", "울릉군");

    private final String code;
    private final String name;

    /**
     * 코드 값으로 ENUM 상수를 찾기
     * @param code 법정동 코드
     * @return GyeongsangbukGuGunCode
     */
    public static GyeongsangbukGuGunCode fromCode(String code) {
        for (GyeongsangbukGuGunCode gu : values()) {
            if (gu.getCode().equals(code)) {
                return gu;
            }
        }
        throw new IllegalArgumentException("Invalid Gyeongsangbuk GuGunCode: " + code);
    }
}