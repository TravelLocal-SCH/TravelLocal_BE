package sch.travellocal.domain.place.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sch.travellocal.common.exception.custom.ApiException;
import sch.travellocal.common.exception.error.ErrorCode;
import sch.travellocal.domain.place.dto.request.VerifyLocationRequestDto;
import sch.travellocal.domain.place.entity.Place;
import sch.travellocal.domain.place.entity.PlaceUserPermission;
import sch.travellocal.domain.place.repository.PlaceRepository;
import sch.travellocal.domain.place.repository.PlaceUserPermissionRepository;
import sch.travellocal.domain.user.entity.User;
import sch.travellocal.domain.user.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class PlacePermissionService {

    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;
    private final PlaceUserPermissionRepository placeUserPermissionRepository;

    // 지구의 반지름
    private static final double EARTH_RADIUS_KM = 6371;

    public String verifyAndGrantPermission(VerifyLocationRequestDto requestDto, String currentUsername) {

        // DB에서 장소 정보 조회
        Place place = placeRepository.findByGooglePlaceId(requestDto.getGooglePlaceId())
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 장소를 찾을 수 없습니다."));

        // Haversine 공식을 사용하여 거리 계산
        double distance = calculateDistance(
                requestDto.getUserLat(), requestDto.getUserLon(),
                place.getLat(), place.getLon()
        );

        // 계산된 거리가 요청된 반경 내에 있는지 확인
        if (distance > requestDto.getRadiusInKm()) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "사용자가 지정된 장소의 반경 내에 없어 권한 요청이 실패했습니다.");
        }

        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ApiException(ErrorCode.TOKEN_NOT_PROVIDED, "로그인에 성공하였지만 DB에 username이 존재하지 않습니다."));

        // 이미 권한이 부여되었는지 확인하여 중복 저장 방지
        if (placeUserPermissionRepository.existsByUserAndPlace(user, place)) {
            throw new ApiException(ErrorCode.DUPLICATE_RESOURCE, "이미 해당 장소에 대한 방문 권한이 있습니다.");
        }

        // 검증 성공 시, 권한 정보 생성 및 저장
        PlaceUserPermission permission = PlaceUserPermission.builder()
                .user(user)
                .place(place)
                .build();
        placeUserPermissionRepository.save(permission);

        return "성공적으로 위치가 검증되었고, 권한이 부여되었습니다.";
    }

    /**
     * 두 지점의 위도, 경도를 사용하여 Haversine 공식으로 거리를 계산합니다.
     * @return 두 지점 사이의 거리 (km)
     */
    private double calculateDistance(double userLat, double userLon, double placeLat, double placeLon) {

        // 모든 좌표를 라디안으로 변환
        double lat1Rad = Math.toRadians(userLat);
        double lon1Rad = Math.toRadians(userLon);
        double lat2Rad = Math.toRadians(placeLat);
        double lon2Rad = Math.toRadians(placeLon);

        // 위도와 경도의 차이 계산
        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = lon2Rad - lon1Rad;

        // Haversine 공식의 중간 계산
        double a = Math.pow(Math.sin(deltaLat / 2), 2) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.pow(Math.sin(deltaLon / 2), 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // 최종 거리 계산
        return EARTH_RADIUS_KM * c;
    }
}