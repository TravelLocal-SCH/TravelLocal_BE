package sch.travellocal.domain.place.enums.gugun;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sch.travellocal.domain.place.enums.LawAddressCode;

/**
 * 충청남도 시군구 법정동 코드 ENUM
 */
@Getter
@RequiredArgsConstructor
public enum ChungcheongnamGuGunCode implements LawAddressCode {
    CHEONAN("130", "천안시"),
    CHEONAN_DONGNAM("131", "천안시 동남구"),
    CHEONAN_SEOBUK("133", "천안시 서북구"),

    GONGJU("150", "공주시"),
    BORYEONG("180", "보령시"),
    ASAN("200", "아산시"),
    SEOSAN("210", "서산시"),
    NONSAN("230", "논산시"),
    GYERYONG("250", "계룡시"),
    DANGJIN("270", "당진시"),

    GEUMSAN("710", "금산군"),
    BUYEO("760", "부여군"),
    SEOCHON("770", "서천군"),
    CHEONGYANG("790", "청양군"),
    HONGSEONG("800", "홍성군"),
    YESAN("810", "예산군"),
    TAEAN("825", "태안군");

    private final String code;
    private final String name;

    /**
     * 코드 값으로 ENUM 상수를 찾기
     * @param code 법정동 코드
     * @return ChungcheongnamGuGunCode
     */
    public static ChungcheongnamGuGunCode fromCode(String code) {
        for (ChungcheongnamGuGunCode gu : values()) {
            if (gu.getCode().equals(code)) {
                return gu;
            }
        }
        throw new IllegalArgumentException("Invalid Chungcheongnam GuGunCode: " + code);
    }
}