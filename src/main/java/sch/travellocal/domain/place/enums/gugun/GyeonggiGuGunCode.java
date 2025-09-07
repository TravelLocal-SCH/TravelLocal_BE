package sch.travellocal.domain.place.enums.gugun;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sch.travellocal.domain.place.enums.LawAddressCode;

/**
 * 경기도 시군구 법정동 코드 ENUM
 */
@Getter
@RequiredArgsConstructor
public enum GyeonggiGuGunCode implements LawAddressCode {
    SUWON("110", "수원시"),
    SUWON_JANGAN("111", "수원시 장안구"),
    SUWON_GWONSEON("113", "수원시 권선구"),
    SUWON_PALDAL("115", "수원시 팔달구"),
    SUWON_YEONGTONG("117", "수원시 영통구"),

    SEONGNAM("130", "성남시"),
    SEONGNAM_SUJEONG("131", "성남시 수정구"),
    SEONGNAM_JUNGWON("133", "성남시 중원구"),
    SEONGNAM_BUNDANG("135", "성남시 분당구"),

    UIJEONGBU("150", "의정부시"),

    ANYANG("170", "안양시"),
    ANYANG_MANAN("171", "안양시 만안구"),
    ANYANG_DONGAN("173", "안양시 동안구"),

    BUCHEON("190", "부천시"),
    BUCHEON_WONMI("192", "부천시 원미구"),
    BUCHEON_SOSA("194", "부천시 소사구"),
    BUCHEON_OJEONG("196", "부천시 오정구"),

    GWANGMYEONG("210", "광명시"),
    PYEONGTAEK("220", "평택시"),
    DONGDUCHEON("250", "동두천시"),

    ANSAN("270", "안산시"),
    ANSAN_SANGROK("271", "안산시 상록구"),
    ANSAN_DANWON("273", "안산시 단원구"),

    GOYANG("280", "고양시"),
    GOYANG_DEOKYANG("281", "고양시 덕양구"),
    GOYANG_ILSANDONG("285", "고양시 일산동구"),
    GOYANG_ILSANSEO("287", "고양시 일산서구"),

    GWACHEON("290", "과천시"),
    GURI("310", "구리시"),
    NAMYANGJU("360", "남양주시"),
    OSAN("370", "오산시"),
    SIHEUNG("390", "시흥시"),
    GUNPO("410", "군포시"),
    UIWANG("430", "의왕시"),
    HANAM("450", "하남시"),

    YONGIN("460", "용인시"),
    YONGIN_CHEOIN("461", "용인시 처인구"),
    YONGIN_GIHEUNG("463", "용인시 기흥구"),
    YONGIN_SUJI("465", "용인시 수지구"),

    PAJU("480", "파주시"),
    ICHEON("500", "이천시"),
    ANSEONG("550", "안성시"),
    GIMPO("570", "김포시"),
    HWASEONG("590", "화성시"),
    GWANGJU("610", "광주시"),
    YANGJU("630", "양주시"),
    POCHEON("650", "포천시"),
    YEOJU("670", "여주시"),

    YEONCHEON("800", "연천군"),
    GAPYEONG("820", "가평군"),
    YANGPYEONG("830", "양평군");

    private final String code;
    private final String name;

    /**
     * 코드 값으로 ENUM 상수를 찾기
     * @param code 법정동 코드
     * @return GyeonggiGuGunCode
     */
    public static GyeonggiGuGunCode fromCode(String code) {
        for (GyeonggiGuGunCode gu : values()) {
            if (gu.getCode().equals(code)) {
                return gu;
            }
        }
        throw new IllegalArgumentException("Invalid Gyeonggi GuGunCode: " + code);
    }
}
