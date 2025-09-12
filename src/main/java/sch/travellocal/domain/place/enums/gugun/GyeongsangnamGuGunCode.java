package sch.travellocal.domain.place.enums.gugun;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sch.travellocal.domain.place.enums.LawAddressCode;

/**
 * 경상남도 시군구 법정동 코드 ENUM
 */
@Getter
@RequiredArgsConstructor
public enum GyeongsangnamGuGunCode implements LawAddressCode {
    CHANGWON("120", "창원시"),
    CHANGWON_UICHANGGU("121", "창원시 의창구"),
    CHANGWON_SEONGSANGU("123", "창원시 성산구"),
    CHANGWON_MASANHAPPOGU("125", "창원시 마산합포구"),
    CHANGWON_MASANHOEWONGU("127", "창원시 마산회원구"),
    CHANGWON_JINHAEGU("129", "창원시 진해구"),
    JINJU("170", "진주시"),
    TONGYEONG("220", "통영시"),
    SACHEON("240", "사천시"),
    GIMHAE("250", "김해시"),
    MIRYANG("270", "밀양시"),
    GEOJE("310", "거제시"),
    YANGSAN("330", "양산시"),

    UIRYEONG("720", "의령군"),
    HAMAN("730", "함안군"),
    CHANGNYEONG("740", "창녕군"),
    GOSEONG("820", "고성군"),
    NAMHAE("840", "남해군"),
    HADONG("850", "하동군"),
    SANCHEONG("860", "산청군"),
    HAMYANG("870", "함양군"),
    GEOCHANG("880", "거창군"),
    HAPCHEON("890", "합천군");

    private final String code;
    private final String name;

    /**
     * 코드 값으로 ENUM 상수를 찾기
     * @param code 법정동 코드
     * @return GyeongsangnamGuGunCode
     */
    public static GyeongsangnamGuGunCode fromCode(String code) {
        for (GyeongsangnamGuGunCode gu : values()) {
            if (gu.getCode().equals(code)) {
                return gu;
            }
        }
        throw new IllegalArgumentException("Invalid Gyeongsangnam GuGunCode: " + code);
    }
}