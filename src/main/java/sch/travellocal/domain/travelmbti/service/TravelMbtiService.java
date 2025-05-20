package sch.travellocal.domain.travelmbti.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sch.travellocal.common.exception.custom.ApiException;
import sch.travellocal.common.exception.error.ErrorCode;
import sch.travellocal.common.response.SuccessResponse;
import sch.travellocal.domain.travelmbti.dto.ResponseDetailMbtiDTO;
import sch.travellocal.domain.travelmbti.dto.ResponseSimpleMbtiDTO;
import sch.travellocal.domain.travelmbti.dto.base.TravelMbtiDTO;
import sch.travellocal.domain.travelmbti.entity.TravelMbti;
import sch.travellocal.domain.travelmbti.entity.TravelMbtiHashtag;
import sch.travellocal.domain.travelmbti.entity.TravelMbtiRegion;
import sch.travellocal.domain.travelmbti.repository.TravelMbtiHashtagRepository;
import sch.travellocal.domain.travelmbti.repository.TravelMbtiRegionRepository;
import sch.travellocal.domain.travelmbti.repository.TravelMbtiRepository;
import sch.travellocal.domain.user.entity.User;
import sch.travellocal.domain.user.service.SecurityUserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TravelMbtiService {

    private final SecurityUserService securityUserService;
    private final TravelMbtiRepository travelMbtiRepository;
    private final TravelMbtiHashtagRepository travelMbtiHashtagRepository;
    private final TravelMbtiRegionRepository travelMbtiRegionRepository;

    public String saveTravelMbti(TravelMbtiDTO travelMbtiDTO) {

        // 현재 로그인한 사용자 정보를 가져와 TravelMbti 엔티티 생성 및 저장
        User user = securityUserService.getUserByJwt();

        TravelMbti mbti = TravelMbti.builder()
                .mbti(travelMbtiDTO.getTravelMbti())
                .user(user)
                .build();
        travelMbtiRepository.save(mbti);

        // 전달받은 해시태그 리스트를 TravelMbtiHashtag 엔티티로 변환 후 저장
        List<TravelMbtiHashtag> hashtags = travelMbtiDTO.getHashtags().stream()
                .map(hashtag -> TravelMbtiHashtag.builder()
                        .hashtag(hashtag)
                        .travelMbti(mbti)
                        .build())
                .toList();
        travelMbtiHashtagRepository.saveAll(hashtags);

        // 전달받은 지역 리스트를 TravelMbtiRegion 엔티티로 변환 후 저장
        List<TravelMbtiRegion> regions = travelMbtiDTO.getRegions().stream()
                .map(region -> TravelMbtiRegion.builder()
                        .region(region)
                        .travelMbti(mbti)
                        .build())
                .toList();
        travelMbtiRegionRepository.saveAll(regions);

        return "success save";
    }

    @Transactional(readOnly = true)
    public List<ResponseSimpleMbtiDTO> getAllTravelMbti() {

        User user = securityUserService.getUserByJwt();

        // 사용자의 TravelMbti를 생성일 역순으로 조회
        List<TravelMbti> travelMbtiList = travelMbtiRepository.findAllByUserOrderByCreatedAtDesc(user)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));

        // 조회된 TravelMbti 리스트를 DTO 리스트로 변환
        List<ResponseSimpleMbtiDTO> responseSimpleMbtiDTOS = travelMbtiList.stream()
                .map(mbti -> ResponseSimpleMbtiDTO.builder()
                        .mbtiId(mbti.getId())
                        .mbti(mbti.getMbti())
                        .build())
                .toList();

        return responseSimpleMbtiDTOS;
    }

    @Transactional(readOnly = true)
    public ResponseDetailMbtiDTO getDetailTravelMbti(Long mbtiId, String mbti) {

        TravelMbti travelMbti = travelMbtiRepository.findById(mbtiId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));

        // 요청한 mbti 값과 DB에 저장된 값이 일치하는지 검증
        if (!travelMbti.getMbti().equals(mbti)) {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }

        // TravelMbti에 연관된 해시태그와 지역 정보를 조회
        List<String> travelMbtiHashtags = travelMbtiHashtagRepository.findAllByTravelMbti(travelMbti)
                .map(hashtags -> hashtags.stream()
                        .map(TravelMbtiHashtag::getHashtag)
                        .toList())
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));

        List<String> travelMbtiRegions = travelMbtiRegionRepository.findAllByTravelMbti(travelMbti)
                .map(regions -> regions.stream()
                        .map(TravelMbtiRegion::getRegion)
                        .toList())
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));

        // 위의 정보를 담은 DTO를 생성하여 응답
        return ResponseDetailMbtiDTO.builder()
                .mbti(mbti)
                .hashtags(travelMbtiHashtags)
                .regions(travelMbtiRegions)
                .build();
    }

    public String deleteTravelMbti(Long mbtiId, String mbti) {

        TravelMbti travelMbti = travelMbtiRepository.findById(mbtiId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));

        // 요청한 mbti 값과 DB에 저장된 값이 일치하는지 검증
        if (!travelMbti.getMbti().equals(mbti)) {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }

        travelMbtiRepository.delete(travelMbti);

        return "success delete";
    }
}
