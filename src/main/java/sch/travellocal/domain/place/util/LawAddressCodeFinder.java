package sch.travellocal.domain.place.util;

import sch.travellocal.domain.place.enums.gugun.*;
import sch.travellocal.domain.place.enums.LawAddressCode;
import sch.travellocal.domain.place.enums.sido.SidoCode;

import java.util.Map;

/**
 * 시도 + 시군구 통합 검색 도우미
 */
public class LawAddressCodeFinder {

    private static final Map<String, LawAddressCode[]> SIDO_GUGUN_ENUM_MAP = Map.ofEntries(
            Map.entry("11", SeoulGuGunCode.values()),
            Map.entry("26", BusanGuGunCode.values()),
            Map.entry("27", DaeguGuGunCode.values()),
            Map.entry("28", IncheonGuGunCode.values()),
            Map.entry("29", GwangjuGuGunCode.values()),
            Map.entry("30", DaejeonGuGunCode.values()),
            Map.entry("31", UlsanGuGunCode.values()),
            Map.entry("41", GyeonggiGuGunCode.values()),
            Map.entry("43", ChungcheongbukGuGunCode.values()),
            Map.entry("44", ChungcheongnamGuGunCode.values()),
            Map.entry("46", JeollanamGuGunCode.values()),
            Map.entry("47", GyeongsangbukGuGunCode.values()),
            Map.entry("48", GyeongsangnamGuGunCode.values()),
            Map.entry("50", JejuGuGunCode.values()),
            Map.entry("51", GangwonGuGunCode.values()),
            Map.entry("52", JeonbukGuGunCode.values()),
            Map.entry("36110", SejongGuGunCode.values())
    );

    public static LawAddressCode findSidoByName(String sidoName) {
        for (SidoCode sido : SidoCode.values()) {
            if (sidoName != null && sidoName.contains(sido.getName())) return sido;
        }
        return null;
    }

    public static LawAddressCode findGuGunByName(String sidoCode, String gugunName) {
        if (sidoCode == null || gugunName == null) return null;
        LawAddressCode[] codes = SIDO_GUGUN_ENUM_MAP.get(sidoCode);
        if (codes == null) return null;

        for (LawAddressCode gu : codes) {
            if (gugunName.contains(gu.getName())) return gu;
        }
        return null;
    }
}
