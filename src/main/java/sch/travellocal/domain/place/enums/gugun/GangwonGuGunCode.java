package sch.travellocal.domain.place.enums.gugun;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sch.travellocal.domain.place.enums.LawAddressCode;

/**
 * 강원특별자치도 시군구 법정동 코드 ENUM
 */
@Getter
@RequiredArgsConstructor
public enum GangwonGuGunCode implements LawAddressCode {
    CHUNCHEON("110", "춘천시"),
    WONJU("130", "원주시"),
    GANGNEUNG("150", "강릉시"),
    DONGHAE("170", "동해시"),
    TAEBAEK("190", "태백시"),
    SOKCHO("210", "속초시"),
    SAMCHEOK("230", "삼척시"),
    HONGCHEON("720", "홍천군"),
    HOENGSEONG("730", "횡성군"),
    YEONGWOL("750", "영월군"),
    PYEONGCHANG("760", "평창군"),
    JEONGSEON("770", "정선군"),
    CHEORWON("780", "철원군"),
    HWACHEON("790", "화천군"),
    YANGGU("800", "양구군"),
    INJE("810", "인제군"),
    GOSEONG("820", "고성군"),
    YANGYANG("830", "양양군");

    private final String code;
    private final String name;

    /**
     * 코드 값으로 ENUM 상수를 찾기
     * @param code 법정동 코드
     * @return GangwonGuGunCode
     */
    public static GangwonGuGunCode fromCode(String code) {
        for (GangwonGuGunCode gu : values()) {
            if (gu.getCode().equals(code)) {
                return gu;
            }
        }
        throw new IllegalArgumentException("Invalid Gangwon GuGunCode: " + code);
    }
}