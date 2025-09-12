package sch.travellocal.domain.place.enums.gugun;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sch.travellocal.domain.place.enums.LawAddressCode;

/**
 * 서울특별시의 자치구 법정동 코드 ENUM
 */
@Getter
@RequiredArgsConstructor
public enum SeoulGuGunCode implements LawAddressCode {
    JONGNO("110", "종로구"),
    JUNG("140", "중구"),
    YONGSAN("170", "용산구"),
    SEONGDONG("200", "성동구"),
    GWANGJIN("215", "광진구"),
    DONGDAEMUN("230", "동대문구"),
    JUNGRANG("260", "중랑구"),
    SEONGBUK("290", "성북구"),
    GANGBUK("305", "강북구"),
    DOBONG("320", "도봉구"),
    NOWON("350", "노원구"),
    EUNPYEONG("380", "은평구"),
    SEODAEMUN("410", "서대문구"),
    MAPO("440", "마포구"),
    YANGCHEON("470", "양천구"),
    GANGSEO("500", "강서구"),
    GURO("530", "구로구"),
    GEUMCHEON("545", "금천구"),
    YEONGDEUNGPO("560", "영등포구"),
    DONGJAK("590", "동작구"),
    GWANAK("620", "관악구"),
    SEOCHO("650", "서초구"),
    GANGNAM("680", "강남구"),
    SONGPA("710", "송파구"),
    GANGDONG("740", "강동구");

    private final String code;
    private final String name;

    public static SeoulGuGunCode fromCode(String code) {
        for (SeoulGuGunCode gu : values()) {
            if (gu.getCode().equals(code)) {
                return gu;
            }
        }
        throw new IllegalArgumentException("Invalid Seoul GuGunCode: " + code);
    }
}