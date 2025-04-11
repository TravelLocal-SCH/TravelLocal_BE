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
public class TravelMbtiService {

    private final SecurityUserService securityUserService;
    private final TravelMbtiRepository travelMbtiRepository;
    private final TravelMbtiHashtagRepository travelMbtiHashtagRepository;
    private final TravelMbtiRegionRepository travelMbtiRegionRepository;

    @Transactional
    public ResponseEntity<?> saveTravelMbti(TravelMbtiDTO travelMbtiDTO) {

        User user = securityUserService.getUserByJwt();

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

    @Transactional
    public ResponseEntity<?> getAllTravelMbti() {

        User user = securityUserService.getUserByJwt();

        List<TravelMbti> travelMbtiList = travelMbtiRepository.findAllByUserOrderByCreatedAtDesc(user)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));

        List<ResponseSimpleMbtiDTO> responseSimpleMbtiDTOS = travelMbtiList.stream()
                .map(mbti -> ResponseSimpleMbtiDTO.builder()
                        .mbtiId(mbti.getId())
                        .mbti(mbti.getMbti())
                        .build())
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(responseSimpleMbtiDTOS);
    }

    @Transactional
    public ResponseEntity<?> getDetailTravelMbti(Long mbtiId, String mbti) {

        TravelMbti travelMbti = travelMbtiRepository.findById(mbtiId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));

        if (!travelMbti.getMbti().equals(mbti)) {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }

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

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDetailMbtiDTO.builder()
                .mbti(mbti)
                .hashtags(travelMbtiHashtags)
                .regions(travelMbtiRegions)
                .build());
    }

    @Transactional
    public ResponseEntity<?> deleteTravelMbti(Long mbtiId, String mbti) {

        TravelMbti travelMbti = travelMbtiRepository.findById(mbtiId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));

        if (!travelMbti.getMbti().equals(mbti)) {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }

        travelMbtiRepository.delete(travelMbti);

        return ResponseEntity.ok(SuccessResponse.ok("success delete"));
    }
}
