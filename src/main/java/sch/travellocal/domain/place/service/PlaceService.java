package sch.travellocal.domain.place.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceReviewService placeReviewService;
    private final PlaceCountRepository placeCountRepository;
    private final RestTemplate restTemplate;
    private final PlaceRepository placeRepository;
    private final ObjectMapper objectMapper;

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
     * @param placeName 장소 이름
     * @param googlePlaceId 구글 Place ID
     * @param language 언어 코드 (kor, eng, jpn)
     * @return 장소 상세 정보 응답
     */
    @Cacheable(value = "placeDetail", key = "#googlePlaceId + '_' + #language", unless = "#result == null")
    public PlaceDetailResponse getPlaceDetail(String placeName, String googlePlaceId, String language) {

        GoogleApiResultDto googleDetail = getGooglePlaceDetailFromGoogle(googlePlaceId);

        String phoneNumber = googleDetail.getFormattedPhoneNumber();
        String openingHours = (googleDetail.getOpeningHours() != null)
                ? String.join(", ", googleDetail.getOpeningHours().getWeekdayText())
                : null;

        String sidoName = extractSidoName(googleDetail.getAddressComponents());
        String gugunName = extractGuGunName(googleDetail.getAddressComponents());
        LawAddressCode sidoCode = LawAddressCodeFinder.findSidoByName(sidoName);
        LawAddressCode gugunCode = LawAddressCodeFinder.findGuGunByName(
                (sidoCode != null) ? sidoCode.getCode() : null,
                gugunName
        );
        String contentId = getTourApiContentIdFromTourApi(
                placeName,
                (sidoCode != null) ? sidoCode.getCode() : null,
                (gugunCode != null) ? gugunCode.getCode() : null,
                language
        );
        TourApiPlaceInfoDto tourInfo = null;
        if (StringUtils.hasText(contentId)) {
            tourInfo = getTourApiPlaceDetailFromTourApi(contentId, language);
            if (tourInfo != null) {
                tourInfo.setLink(extractHrefLink(tourInfo.getLink()));
            }
        } else {
            log.warn("TourAPI에서 '{}'에 대한 contentId를 찾을 수 없습니다.", placeName);
        }

        List<PlaceReviewResponseDto> placeReviewResponseDtos = placeReviewService.getReviewsByPlace(googlePlaceId, new GetPlaceReviewsRequestDto());
        Place place = placeRepository.findByGooglePlaceId(googlePlaceId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "place not found"));
        PlaceCount placeCount = placeCountRepository.findByPlace(place)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "placeCount not found"));


        return PlaceDetailResponse.builder()
                .tourApiPlaceInfo(tourInfo)
                .googlePlaceInfo(GooglePlaceInfoDto.builder()
                        .openingHours(openingHours)
                        .phone(phoneNumber)
                        .build())
                .googleEvaluation(toGoogleDto(googleDetail))
                .travelLocalEvaluation(TravelLocalEvaluationDto.builder()
                        .reviewCount(placeCount.getReviewCount())
                        .rating((placeCount.getReviewCount() == 0) ? 0F : placeCount.getSumRating() / (float) placeCount.getReviewCount())
                        .reviews(placeReviewResponseDtos)
                        .build())
                .build();
    }

    private GoogleApiResultDto getGooglePlaceDetailFromGoogle(String googlePlaceId) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl("https://maps.googleapis.com/maps/api/place/details/json")
                .queryParam("place_id", googlePlaceId)
                .queryParam("key", googleMapApiKey)
                .queryParam("fields", "name,rating,user_ratings_total,address_components,url,opening_hours,formatted_phone_number")
                .queryParam("language", "ko")
                .build(true)
                .toUri();

        try {
            String response = restTemplate.getForObject(uri, String.class);
            return objectMapper.readValue(response, GoogleApiResultWrapper.class).getResult();
        } catch (Exception e) {
            log.error("Google API 파싱 실패: {}", e.getMessage());
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR, "Google API 연동 중 오류가 발생했습니다.");
        }
    }

    private String getTourApiContentIdFromTourApi(String keyword, String sidoCode, String gugunCode, String language) {

        String baseUrl = resolveTourApiBaseUrl(language);
        String base = makeBaseUrl(baseUrl, "/searchKeyword2", tourApiKey);
        URI uri = UriComponentsBuilder
                .fromHttpUrl(base)
                .queryParam("keyword", getEncodedUTF_8(keyword))
                .queryParam("lDongRegnCd", sidoCode)
                .queryParam("lDongSignguCd", gugunCode)
                .build(true)
                .toUri();

        try {
            String response = restTemplate.getForObject(uri, String.class);
            return objectMapper.readValue(response, TourApiSearchResponseWrapper.class).getFirstContentId();
        } catch (Exception e) {
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR, "TourAPI /searchKeyword2 연동 중 오류가 발생했습니다.");
        }
    }

    private TourApiPlaceInfoDto getTourApiPlaceDetailFromTourApi(String contentId, String language) {

        String baseUrl = resolveTourApiBaseUrl(language);
        String base = makeBaseUrl(baseUrl, "/detailCommon2", tourApiKey);
        URI uri = UriComponentsBuilder
                .fromHttpUrl(base)
                .queryParam("contentId", contentId)
                .build(true)
                .toUri();

        try {
            String response = restTemplate.getForObject(uri, String.class);
            TourApiPlaceInfoDto dto = objectMapper.readValue(response, TourApiDetailWrapper.class).toDto();
            if (dto != null) dto.setLink(extractHrefLink(dto.getLink()));
            return dto;
        } catch (Exception e) {
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR, "TourAPI /detailCommon2 연동 중 오류가 발생했습니다.");
        }
    }

    public Place getOrCreatePlaceAndInitCount(TourProgramScheduleDto scheduleDto) {
        return placeRepository.findByGooglePlaceId(scheduleDto.getGooglePlaceId())
                .orElseGet(() -> {
                    Place newPlace = placeRepository.save(
                            Place.builder()
                                    .googlePlaceId(scheduleDto.getGooglePlaceId())
                                    .name(scheduleDto.getPlaceName())
                                    .lat(scheduleDto.getLat())
                                    .lon(scheduleDto.getLon())
                                    .build());
                    placeCountRepository.save(
                            PlaceCount.builder()
                                    .reviewCount(0)
                                    .sumRating(0F)
                                    .place(newPlace)
                                    .build());
                    return newPlace;
                });
    }

    private String resolveTourApiBaseUrl(String language) {
        return switch (language.toLowerCase()) {
            case "eng" -> ENG_BASE_URL;
            case "jpn" -> JPN_BASE_URL;
            default -> KOR_BASE_URL;
        };
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

    private String extractSidoName(List<GoogleApiResultDto.AddressComponent> components) {
        if (components == null) return null;
        return components.stream()
                .filter(c -> c.getTypes().contains("administrative_area_level_1"))
                .map(GoogleApiResultDto.AddressComponent::getLongName)
                .findFirst().orElse(null);
    }

    private String extractGuGunName(List<GoogleApiResultDto.AddressComponent> components) {
        if (components == null)
            return null;
        return components.stream()
                .filter(c -> c.getTypes().contains("locality") || c.getTypes().contains("sublocality_level_1"))
                .map(GoogleApiResultDto.AddressComponent::getLongName)
                .findFirst().orElse(null);
    }

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
