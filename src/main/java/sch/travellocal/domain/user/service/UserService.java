package sch.travellocal.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sch.travellocal.auth.oauth.CustomOAuth2User;
import sch.travellocal.common.exception.custom.ApiException;
import sch.travellocal.common.exception.error.ErrorCode;
import sch.travellocal.domain.user.dto.request.UserInfoRequestDto;
import sch.travellocal.domain.user.dto.response.UserInfoResponseDto;
import sch.travellocal.domain.user.entity.User;
import sch.travellocal.domain.user.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserInfoResponseDto getUserInfo(CustomOAuth2User customOAuth2User) {

        User existUser = userRepository.findByUsername(customOAuth2User.getUsername())
                .orElseThrow(() -> new ApiException(ErrorCode.DATABASE_ERROR));

        return UserInfoResponseDto.builder()
                .id(existUser.getId())
                .username(existUser.getUsername())
                .name(existUser.getName())
                .email(existUser.getEmail())
                .gender(existUser.getGender())
                .birthYear(existUser.getBirthYear())
                .mobile(existUser.getMobile())
                .role(existUser.getRole())
                .protectNumber(existUser.getProtectNumber())
                .build();
    }

    public String updateUserInfo(UserInfoRequestDto userInfoRequestDto, CustomOAuth2User customOAuth2User) {

        User existUser = userRepository.findByUsername(customOAuth2User.getUsername())
                .orElseThrow(() -> new ApiException(ErrorCode.DATABASE_ERROR));

        if (!existUser.getEmail().equals(userInfoRequestDto.getEmail()) && userRepository.existsByEmail(userInfoRequestDto.getEmail())) {
            throw new ApiException(ErrorCode.DUPLICATE_RESOURCE, "이미 존재하는 이메일입니다.");
        }

        existUser.setName(userInfoRequestDto.getName());
        existUser.setGender(userInfoRequestDto.getGender());
        existUser.setBirthYear(userInfoRequestDto.getBirthYear());
        existUser.setMobile(userInfoRequestDto.getMobile());
        existUser.setRole(userInfoRequestDto.getRole());
        existUser.setProtectNumber(userInfoRequestDto.getProtectNumber());
        existUser.setName(userInfoRequestDto.getName());

        return "사용자 정보 수정이 완료되었습니다.";
    }
}
