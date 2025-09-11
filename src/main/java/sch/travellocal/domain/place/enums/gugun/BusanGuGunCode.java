package sch.travellocal.domain.place.enums.gugun;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sch.travellocal.domain.place.enums.LawAddressCode;

/**
 * 부산광역시의 구군 법정동 코드 ENUM
 */
@Getter
@RequiredArgsConstructor
public enum BusanGuGunCode implements LawAddressCode {
    JUNG("110", "중구"),
    SEO("140", "서구"),
    DONG("170", "동구"),
    YEONGDO("200", "영도구"),
    BUSANJIN("230", "부산진구"),
    DONGNAE("260", "동래구"),
    NAM("290", "남구"),
    BUK("320", "북구"),
    HAEUNDAE("350", "해운대구"),
    SAHA("380", "사하구"),
    GEUMJEONG("410", "금정구"),
    GANGSEO("440", "강서구"),
    YEONJE("470", "연제구"),
    SUYEONG("500", "수영구"),
    SASANG("530", "사상구"),
    GIJANG("710", "기장군");

    private final String code;
    private final String name;

    public static BusanGuGunCode fromCode(String code) {
        for (BusanGuGunCode gu : values()) {
            if (gu.getCode().equals(code)) {
                return gu;
            }
        }
        throw new IllegalArgumentException("Invalid Busan GuGunCode: " + code);
    }
}
