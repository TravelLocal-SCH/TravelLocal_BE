package sch.travellocal.auth.oauth;

import io.lettuce.core.AbstractRedisAsyncCommands;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import sch.travellocal.auth.dto.GoogleResponse;
import sch.travellocal.auth.dto.KakaoResponse;
import sch.travellocal.auth.dto.NaverResponse;
import sch.travellocal.auth.dto.OAuth2Response;
import sch.travellocal.common.exception.custom.AuthException;
import sch.travellocal.common.exception.error.ErrorCode;
import sch.travellocal.domain.point.entity.UserPoint;
import sch.travellocal.domain.point.repository.UserPointRepository;
import sch.travellocal.domain.user.dto.UserDTO;
import sch.travellocal.domain.user.entity.User;
import sch.travellocal.domain.user.enums.UserRole;
import sch.travellocal.domain.user.repository.UserRepository;

@Service
@AllArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
//    private final UserPointRepository userPointRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = resolveOAuth2Response(registrationId, oAuth2User);

        if (oAuth2Response == null) {
            throw new AuthException(ErrorCode.UNSUPPORTED_PROVIDER);
        }

        String username = oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId();

        User user = userRepository.findByUsername(username)
                // 사용자 정보 수정은 사용자만 가능하도록, 해당 로직 냅두면 소셜로그인 시에 다시 덮어씌어짐
                .orElseGet(() -> User.builder()
                                .username(username)
                                .name(oAuth2Response.getName())
                                .email(oAuth2Response.getEmail())
                                .gender(oAuth2Response.getGender())
                                .birthYear(oAuth2Response.getBirthYear())
                                .mobile(oAuth2Response.getMobile())
                        // 여행 프로그램을 등록하기 전까진 CONSUMER
                        // 원래 권한마다 사용 가능한 기능 부여할 예정이였으나 현재는 보류
                                .role(UserRole.GUIDE_CONSUMER)
                                .build()

                        // 회원가입 시 포인트 0으로 초기화
                );

        userRepository.save(user);

//        // 회원가입 시 포인트 3000원으로 초기화
//        userPointRepository.save(UserPoint.builder()
//                .point(3000L)
//                .user(user)
//                .build());

        return new CustomOAuth2User(
                UserDTO.builder()
                    .username(user.getUsername())
                    .name(user.getName())
                    .role(user.getRole())
                    .build()
        );
    }

    private OAuth2Response resolveOAuth2Response(String registrationId, OAuth2User oAuth2User) {

        if (registrationId.equals("naver")) {
            return new NaverResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("kakao")) {
            System.out.println("oAuth2User: " + oAuth2User);
            System.out.println("oAuth2User: " + oAuth2User.getAttributes());
            return new KakaoResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("google")) {
            System.out.println("oAuth2User: " + oAuth2User);
            System.out.println("oAuth2User: " + oAuth2User.getAttributes());
            return new GoogleResponse(oAuth2User.getAttributes());
        }
        return null;
    }
}
