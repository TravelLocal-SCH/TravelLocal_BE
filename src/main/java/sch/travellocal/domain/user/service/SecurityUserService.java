package sch.travellocal.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sch.travellocal.common.exception.custom.ApiException;
import sch.travellocal.common.exception.error.ErrorCode;
import sch.travellocal.domain.user.entity.User;
import sch.travellocal.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class SecurityUserService {

    private final UserRepository userRepository;

    /**
     * 요청에 담긴 Jwt Token을 통해 로그인한 유저 가져오기
     */
    public User getUserByJwt() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(ErrorCode.DATABASE_ERROR, "로그인에 성공하였지만 DB에 username이 존재하지 않습니다."));

        return user;
    }
}
