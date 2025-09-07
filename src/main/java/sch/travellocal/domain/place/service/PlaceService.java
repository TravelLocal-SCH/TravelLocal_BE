package sch.travellocal.domain.place.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple3;
import sch.travellocal.common.exception.custom.ApiException;
import sch.travellocal.common.exception.error.ErrorCode;
import sch.travellocal.domain.place.dto.GoogleApiResultDto;
import sch.travellocal.domain.place.dto.request.GetPlaceReviewsRequestDto;
import sch.travellocal.domain.place.dto.response.*;
import sch.travellocal.domain.place.dto.wrapper.GoogleApiResultWrapper;
import sch.travellocal.domain.place.dto.wrapper.TourApiDetailWrapper;
import sch.travellocal.domain.place.dto.wrapper.TourApiSearchResponseWrapper;
import sch.travellocal.domain.place.entity.Place;
import sch.travellocal.domain.place.entity.PlaceCount;
import sch.travellocal.domain.place.enums.LawAddressCode;
import sch.travellocal.domain.place.repository.PlaceCountRepository;
import sch.travellocal.domain.place.repository.PlaceRepository;
import sch.travellocal.domain.place.util.LawAddressCodeFinder;
import sch.travellocal.domain.tourprogram.dto.TourProgramScheduleDto;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PlaceService {

    private final PlaceReviewService placeReviewService;
    private final PlaceCountRepository placeCountRepository;

    private final WebClient googleWebClient;
    private final WebClient tourApiWebClient;
    private final PlaceRepository placeRepository;

    @Value("${google.api.key}")
    private String googleMapApiKey;

    @Value("${tourapi.api.key}")
    private String tourApiKey;

    private static final String KOR_BASE_URL = "https://apis.data.go.kr/B551011/KorService2";
    private static final String ENG_BASE_URL = "https://apis.data.go.kr/B551011/EngService2";
    private static final String JPN_BASE_URL = "https://apis.data.go.kr/B551011/JpnService2";
    private static final String MOBILE_OS = "ETC";
    private static final String MOBILE_APP = "TravelLocal";

    /**
     * 각 플랫폼에 API를 통해 장소 상세 정보를 가져오는 메서드
     * @param placeName 장소 이름
     * @param googlePlaceId 구글 Place ID
     * @param language 언어 코드 (kor, eng, jpn)
     * @return 장소 상세 정보 응답
     */
    public Mono<PlaceDetailResponse> getPlaceDetail(String placeName, String googlePlaceId, String language) {

        // google map api 요청
        return getGooglePlaceDetail(googlePlaceId)
                .publishOn(Schedulers.boundedElastic())
                .flatMap(googleDetail -> {

                    String phoneNumber = googleDetail.getFormattedPhoneNumber();
                    String openingHours = googleDetail.getOpeningHours() != null
                            ? String.join(", ", googleDetail.getOpeningHours().getWeekdayText())
                            : null;

                    String sidoName = extractSidoName(googleDetail.getAddressComponents());
                    String gugunName = extractGuGunName(googleDetail.getAddressComponents());

                    LawAddressCode sidoCode = LawAddressCodeFinder.findSidoByName(sidoName);
                    LawAddressCode gugunCode = LawAddressCodeFinder.findGuGunByName(
                            sidoCode != null ? sidoCode.getCode() : null,
                            gugunName
                    );

                    // [실무 수준 리팩토링]
                    // ▶ 블로킹 DB 호출을 논블로킹 체인으로 안전하게 오프로드
                    // ▶ boundedElastic 스케줄러에서 실행
                    Mono<List<PlaceReviewResponseDto>> reviewListMono = Mono.fromCallable(() ->
                            placeReviewService.getReviewsByPlace(googlePlaceId, new GetPlaceReviewsRequestDto())
                    ).subscribeOn(Schedulers.boundedElastic());

                    Mono<Place> placeMono = Mono.fromCallable(() ->
                            placeRepository.findByGooglePlaceId(googlePlaceId)
                                    .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "place not found"))
                    ).subscribeOn(Schedulers.boundedElastic());

                    Mono<PlaceCount> placeCountMono = placeMono.flatMap(place ->
                            Mono.fromCallable(() ->
                                    placeCountRepository.findByPlace(place)
                                            .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "placeCount not found"))
                            ).subscribeOn(Schedulers.boundedElastic())
                    );

                    // ▶ 위 세 개 Mono를 zip으로 결합 → 블로킹 I/O를 분리된 스레드에서 안전하게 처리
                    Mono<Tuple3<List<PlaceReviewResponseDto>, Place, PlaceCount>> travelLocalDataMono =
                            Mono.zip(reviewListMono, placeMono, placeCountMono);

                    // ▶ DB 조회 결과와 TourAPI 검색 결과를 결합
                    return travelLocalDataMono.flatMap(travelLocalTuple -> {
                        List<PlaceReviewResponseDto> placeReviewResponseDtos = travelLocalTuple.getT1();
                        Place place = travelLocalTuple.getT2();
                        PlaceCount placeCount = travelLocalTuple.getT3();

                        return getTourApiContentId(
                                placeName,
                                sidoCode != null ? sidoCode.getCode() : null,
                                gugunCode != null ? gugunCode.getCode() : null,
                                language
                        ).flatMap(contentId ->
                                getTourApiPlaceDetail(contentId, language)
                                        .map(tourInfo -> PlaceDetailResponse.builder()
                                                .tourApiPlaceInfo(tourInfo)
                                                .googlePlaceInfo(GooglePlaceInfoDto.builder()
                                                        .openingHours(openingHours)
                                                        .phone(phoneNumber)
                                                        .build())
                                                .googleEvaluation(toGoogleDto(googleDetail))
                                                .travelLocalEvaluation(TravelLocalEvaluationDto.builder()
                                                        .reviewCount(placeCount.getReviewCount())
                                                        .rating(placeCount.getReviewCount() == 0 ? 0F
                                                                : placeCount.getSumRating() / (float) placeCount.getReviewCount())
                                                        .reviews(placeReviewResponseDtos)
                                                        .build())
                                                .build()
                                        ));
                    });
                });
    }

    /**
     * 구글 Place API를 통해 장소 상세 정보를 가져오는 메서드
     * @param placeId 구글 Place ID
     * @return 구글 장소 상세 정보 DTO
     */
    private Mono<GoogleApiResultDto> getGooglePlaceDetail(String placeId) {

        return googleWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/maps/api/place/details/json")
                        .queryParam("place_id", placeId)
                        .queryParam("key", googleMapApiKey)
                        .queryParam("fields", String.join(",", List.of(
                                "name",
                                "rating",
                                "user_ratings_total",
                                "address_components",
                                "url",
                                "opening_hours",
                                "formatted_phone_number"
                        )))
                        .queryParam("language", "ko")
                        .build())
                .retrieve()
                .bodyToMono(String.class) // 먼저 String으로 받아서 출력
                .doOnNext(json -> System.out.println("[GoogleAPI 응답 RAW JSON] = " + json))
                .flatMap(json -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        GoogleApiResultWrapper wrapper = mapper.readValue(json, GoogleApiResultWrapper.class);
                        return Mono.just(wrapper.getResult());
                    } catch (Exception e) {
                        return Mono.error(new RuntimeException("Google JSON 파싱 실패", e));
                    }
                });
    }

    /**
     * Tour API를 통해 장소의 contentId를 검색하는 메서드
     * @param keyword 장소 이름
     * @param sidoCode 시도 코드
     * @param gugunCode 시군구 코드
     * @return Tour API에서 검색된 첫 번째 contentId
     */
    private Mono<String> getTourApiContentId(String keyword, String sidoCode, String gugunCode, String language) {

        String baseUrl = "";
        if (language.equals("kor")) {
            baseUrl = KOR_BASE_URL;
        } else if (language.equals("eng")) {
            baseUrl = ENG_BASE_URL;
        } else if (language.equals("jpn")) {
            baseUrl = JPN_BASE_URL;
        } else {
            return Mono.error(new IllegalArgumentException("지원하지 않는 언어: " + language));
        }

        String base = makeBaseUrl(baseUrl, "/searchKeyword2", tourApiKey);
        URI uri = UriComponentsBuilder
                .fromHttpUrl(base)
                .queryParam("keyword", getEncodedUTF_8(keyword))
                .queryParam("lDongRegnCd", sidoCode)
                .queryParam("lDongSignguCd", gugunCode)
                .build(true)
                .toUri();

        System.out.println("[TourAPI /searchKeyword2 요청 URI] = " + uri);

        return tourApiWebClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(json -> System.out.println("[TourAPI 검색 응답 RAW JSON] = " + json))
                .flatMap(json -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        TourApiSearchResponseWrapper wrapper = mapper.readValue(json, TourApiSearchResponseWrapper.class);
                        return Mono.just(wrapper.getFirstContentId());
                    } catch (Exception e) {
                        return Mono.error(new RuntimeException("TourAPI 검색 JSON 파싱 실패", e));
                    }
                });
    }

    /**
     * Tour API를 통해 장소의 상세 정보를 가져오는 메서드
     * @param contentId Tour API에서 검색된 장소의 contentId
     * @param language 언어 코드 (kor, eng, jpn)
     * @return Tour API에서 반환된 장소 상세 정보 DTO
     */
    private Mono<TourApiPlaceInfoDto> getTourApiPlaceDetail(String contentId, String language) {

        String baseUrl = "";
        if (language.equals("kor")) {
            baseUrl = KOR_BASE_URL;
        } else if (language.equals("eng")) {
            baseUrl = ENG_BASE_URL;
        } else if (language.equals("jpn")) {
            baseUrl = JPN_BASE_URL;
        } else {
            return Mono.error(new IllegalArgumentException("지원하지 않는 언어: " + language));
        }

        String base = makeBaseUrl(baseUrl, "/detailCommon2", tourApiKey);
        URI uri = UriComponentsBuilder
                .fromHttpUrl(base)
                .queryParam("contentId", contentId)
                .build(true)
                .toUri();

        System.out.println("[TourAPI /detailCommon2 요청 URI] = " + uri);

        return tourApiWebClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(json -> System.out.println("[TourAPI 상세 응답 RAW JSON] = " + json))
                .flatMap(json -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        TourApiDetailWrapper wrapper = mapper.readValue(json, TourApiDetailWrapper.class);
                        return Mono.just(wrapper.toDto())
                                .map(dto -> {
                                    dto.setLink(extractHrefLink(dto.getLink()));
                                    return dto;
                                });
                    } catch (Exception e) {
                        return Mono.error(new RuntimeException("TourAPI 상세 JSON 파싱 실패", e));
                    }
                });
    }

    /**
     * 헬퍼 메서드들
     */

    // 장소가 존재하지 않으면 생성하고 PlaceCount도 초기화하는 메서드
    public Place getOrCreatePlaceAndInitCount(TourProgramScheduleDto scheduleDto) {

        return placeRepository.findByGooglePlaceId(scheduleDto.getGooglePlaceId())
                .orElseGet(() -> {
                    // Place 생성
                    Place newPlace = placeRepository.save(
                            Place.builder()
                                    .googlePlaceId(scheduleDto.getGooglePlaceId())
                                    .name(scheduleDto.getPlaceName())
                                    .lat(scheduleDto.getLat())
                                    .lon(scheduleDto.getLon())
                                    .build());

                    // PlaceCount 생성
                    placeCountRepository.save(
                            PlaceCount.builder()
                                    .reviewCount(0)
                                    .sumRating(0F)
                                    .place(newPlace)
                                    .build());

                    return newPlace;
                });
    }

    private String getEncodedUTF_8(String str) {
        return URLEncoder.encode(str, StandardCharsets.UTF_8);
    }

    private String makeBaseUrl(String baseUrl, String endPoint, String tourApiKey) {
        return baseUrl + endPoint
                + "?serviceKey=" + getEncodedUTF_8(tourApiKey)
                + "&_type=json"
                + "&MobileOS=" + MOBILE_OS
                + "&MobileApp=" + MOBILE_APP;
    }

    private GoogleEvaluationDto toGoogleDto(GoogleApiResultDto google) {
        return GoogleEvaluationDto.builder()
                .reviewCount(google.getReviewCount())
                .rating(google.getRating())
                .googleMapsUrl(google.getUrl())
                .build();
    }

    /**
     * 구글 API에서 시도이름을 추출하는 메서드
     * @param components 구글 API 응답의 주소 구성 요소
     * @return 시도 이름
     */
    private String extractSidoName(List<GoogleApiResultDto.AddressComponent> components) {

        if (components == null) return null;
        for (var c : components) {
            if (c.getTypes().contains("administrative_area_level_1")) {
                System.out.println("[GoogleAPI] Sido Name: " + c.getLongName());
                return c.getLongName();
            }
        }
        return null;
    }

    /**
     * 구글 API에서 구군 이름을 추출하는 메서드
     * @param components 구글 API 응답의 주소 구성 요소
     * @return 구군 이름
     */
    private String extractGuGunName(List<GoogleApiResultDto.AddressComponent> components) {

        if (components == null) return null;
        for (var c : components) {
            if (c.getTypes().contains("locality") || c.getTypes().contains("sublocality_level_1")) {
                System.out.println("[GoogleAPI] GuGun Name: " + c.getLongName());
                return c.getLongName();
            }
        }
        return null;
    }

    /**
     * HTML 링크에서 href 속성의 값을 추출하는 메서드
     * @param htmlLink HTML 링크 문자열
     * @return href 속성의 값
     */
    private String extractHrefLink(String htmlLink) {
        if (htmlLink == null) return null;
        htmlLink = htmlLink.trim();
        if (!htmlLink.startsWith("<a ")) return htmlLink;
        int hrefIndex = htmlLink.indexOf("href=\"");
        if (hrefIndex == -1) return htmlLink;
        int start = hrefIndex + 6;
        int end = htmlLink.indexOf("\"", start);
        if (end == -1) return htmlLink;
        return htmlLink.substring(start, end);
    }
}