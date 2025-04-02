package sch.travellocal.domain.travelmbti.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sch.travellocal.common.exception.custom.ApiException;
import sch.travellocal.common.exception.error.ErrorCode;
import sch.travellocal.common.response.SuccessResponse;
import sch.travellocal.domain.travelmbti.dto.ResponseMbtiListDTO;
import sch.travellocal.domain.travelmbti.dto.base.TravelMbtiDTO;
import sch.travellocal.domain.travelmbti.entity.TravelMbti;
import sch.travellocal.domain.travelmbti.entity.TravelMbtiHashtag;
import sch.travellocal.domain.travelmbti.entity.TravelMbtiRegion;
import sch.travellocal.domain.travelmbti.repository.TravelMbtiHashtagRepository;
import sch.travellocal.domain.travelmbti.repository.TravelMbtiRegionRepository;
import sch.travellocal.domain.travelmbti.repository.TravelMbtiRepository;
import sch.travellocal.domain.user.entity.User;
import sch.travellocal.domain.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TravelMbtiService {

    private final UserRepository userRepository;
    private final TravelMbtiRepository travelMbtiRepository;
    private final TravelMbtiHashtagRepository travelMbtiHashtagRepository;
    private final TravelMbtiRegionRepository travelMbtiRegionRepository;

    @Transactional
    public ResponseEntity<?> saveTravelMbti(TravelMbtiDTO travelMbtiDTO) {

        String username = getUsername();
        System.out.println("username: " + username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(ErrorCode.DATABASE_ERROR, "로그인에 성공하였지만 DB에 username이 존재하지 않습니다."));

        TravelMbti mbti = TravelMbti.builder()
                .mbti(travelMbtiDTO.getTravelMbti())
                .user(user)
                .build();
        travelMbtiRepository.save(mbti);

        List<TravelMbtiHashtag> hashtags = travelMbtiDTO.getHashtags().stream()
                .map(hashtag -> TravelMbtiHashtag.builder()
                        .hashtag(hashtag)
                        .travelMbti(mbti)
                        .build())
                .toList();
        travelMbtiHashtagRepository.saveAll(hashtags);

        List<TravelMbtiRegion> regions = travelMbtiDTO.getRegions().stream()
                .map(region -> TravelMbtiRegion.builder()
                        .region(region)
                        .travelMbti(mbti)
                        .build())
                .toList();
        travelMbtiRegionRepository.saveAll(regions);

        return ResponseEntity.ok(SuccessResponse.ok("success save"));
    }

    private String getUsername() {

        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
