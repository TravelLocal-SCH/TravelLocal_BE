package sch.travellocal.domain.tourprogram.service;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sch.travellocal.common.exception.custom.ApiException;
import sch.travellocal.common.exception.error.ErrorCode;
import sch.travellocal.domain.tourprogram.dto.TourProgramDto;
import sch.travellocal.domain.tourprogram.dto.TourProgramScheduleDto;
import sch.travellocal.domain.tourprogram.dto.TourProgramUserDto;
import sch.travellocal.domain.tourprogram.dto.request.SaveTourProgramRequestDto;
import sch.travellocal.domain.tourprogram.dto.response.TourProgramDetailResponseDto;
import sch.travellocal.domain.tourprogram.entity.*;
import sch.travellocal.domain.tourprogram.repository.*;
import sch.travellocal.domain.user.entity.User;
import sch.travellocal.domain.user.service.SecurityUserService;
import sch.travellocal.upload.repository.ImageRepository;
import sch.travellocal.upload.entity.Image;
import sch.travellocal.upload.enums.ImageTargetType;
import sch.travellocal.upload.service.S3Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TourProgramService {

    private final SecurityUserService securityUserService;
    private final TourProgramRepository tpRepository;
    private final HashtagRepository hashtagRepository;
    private final TourProgramScheduleRepository tpScheduleRepository;
    private final TourProgramCountRepository tpCountRepository;
    private final TourProgramHashtagRepository tpHashtagRepository;
    private final ReviewRepository tpReviewRepository;
    private final WishlistRepository tpWishlistRepository;
    private final ImageRepository imageRepository;
    private final S3Service s3Service;

    public TourProgramDetailResponseDto saveTourProgram(SaveTourProgramRequestDto requestDto) {

        // 현재 로그인한 사용자 정보 get
        User user = securityUserService.getUserByJwt();

        // 게시물 저장
        TourProgram program = TourProgram.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .guidePrice(requestDto.getGuidePrice())
                .region(requestDto.getRegion())
                .thumbnailUrl(requestDto.getThumbnailUrl())
                .user(user)
                .build();
        tpRepository.save(program);

        // 게시물에 대한 해시태그 유무에 따른 처리 [ 존재 시 해당 객체 사용, 존재하지 않는다면 객체 저장 후 사용 ]
        for (String tag : requestDto.getHashtags()) {
            Hashtag hashtag = hashtagRepository.findByName(tag)
                    .orElseGet(() -> hashtagRepository.save(Hashtag.builder().name(tag).build()));
            // 양방향 저장
            program.addHashtag(hashtag);
        }
        tpHashtagRepository.saveAll(program.getTourProgramHashtags());

        // 게시물에 대한 상세 스케줄 저장
        List<TourProgramSchedule> schedules = requestDto.getSchedules().stream()
                .map(scheduleDto -> TourProgramSchedule.builder()
                        .tourProgram(program)
                        .day(scheduleDto.getDay())
                        .scheduleSequence(scheduleDto.getScheduleSequence())
                        .placeName(scheduleDto.getPlaceName())
                        .lat(scheduleDto.getLat())
                        .lon(scheduleDto.getLon())
                        .placeDescription(scheduleDto.getPlaceDescription())
                        .travelTime(scheduleDto.getTravelTime())
                        .build())
                .toList();
        tpScheduleRepository.saveAll(schedules);

        tpCountRepository.save(TourProgramCount.builder()
                .reviewCount(0)
                .wishlistCount(0)
                .tourProgram(program)
                .build());

        // 사용자 입장에서 게시물 생성 이후 화면은 작성한 게시물이 보여지기 때문에 해당 정보 반환
        return TourProgramDetailResponseDto.builder()
                .TourProgramId(program.getId())
                .title(program.getTitle())
                .region(program.getRegion())
                .description(program.getDescription())
                .guidePrice(program.getGuidePrice())
                .thumbnailUrl(program.getThumbnailUrl())
                .hashtags(program.getTourProgramHashtags().stream()
                        .map(h -> h.getHashtag().getName())
                        .toList())
                .schedules(schedules.stream()
                        .map(s -> TourProgramScheduleDto.builder()
                                .day(s.getDay())
                                .scheduleSequence(s.getScheduleSequence())
                                .placeName(s.getPlaceName())
                                .lat(s.getLat())
                                .lon(s.getLon())
                                .placeDescription(s.getPlaceDescription())
                                .travelTime(s.getTravelTime())
                                .build())
                        .toList())
                .user(TourProgramUserDto.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .build())
                .reviewCount(0)
                .wishlistCount(0)
                .build();
    }

    @Transactional(readOnly = true)
    public TourProgramDetailResponseDto getDetailTourProgram(Long tourProgramId) {

        // 게시물Id로 객체 get
        TourProgram tourProgram = tpRepository.findById(tourProgramId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "Tour Program Not Found"));

        // 게시물 생성 시점에 count객체를 생성했기 때문에 존재하지 않는 상황은 DB에 문제있는 상황
        TourProgramCount tourProgramCount = tpCountRepository.findByTourProgramId(tourProgramId)
                .orElseThrow(() -> new ApiException(ErrorCode.DATABASE_ERROR, "Tour Program Count Not Found"));

        // 해시태그 get
        /**
         * tourProgram.getTourProgramHashtags() 시점엔 Lazy여서 쿼리 안나가지만, stream으로 객체가 필요해지는 시점에 쿼리가 트리거됨
         * TourProgram에 대해서 n개의 TourProgramHashtag를 가져오고 각 객체에 대해서 Hashtag를 1개씩 가져오면 n + 1인가?
         * 다만 해당 로직에서 해시태그는 각 게시물마다 3개로 제한을 두기 때문에 n이 3개까지밖에 되지 않아 성능상으로 큰 문제를 주진 않는다고 생각함.
         * 따라서 dto projection이나 join fetch를 하지 않아도 된다고 생각이 듬
         */
        List<String> hashtags = tourProgram.getTourProgramHashtags().stream()
                .map(hashtag -> hashtag.getHashtag().getName()).toList();

        // 상세 프로그램 스케줄 get
        List<TourProgramScheduleDto> tourProgramScheduleDtos = tpScheduleRepository.findAllByTourProgram(tourProgram).stream()
                .map(schedule -> TourProgramScheduleDto.builder()
                        .day(schedule.getDay())
                        .scheduleSequence(schedule.getScheduleSequence())
                        .placeName(schedule.getPlaceName())
                        .lat(schedule.getLat())
                        .lon(schedule.getLon())
                        .placeDescription(schedule.getPlaceDescription())
                        .travelTime(schedule.getTravelTime())
                        .build())
                .toList();

        // 제공되는 data
        // 게시물 상세 정보, 작성자 정보, 리뷰/위시리스트 개수
        TourProgramDetailResponseDto responseDto = TourProgramDetailResponseDto.builder()
                .TourProgramId(tourProgram.getId())
                .title(tourProgram.getTitle())
                .region(tourProgram.getRegion())
                .description(tourProgram.getDescription())
                .guidePrice(tourProgram.getGuidePrice())
                .thumbnailUrl(tourProgram.getThumbnailUrl())
                .wishlistCount(tourProgramCount.getWishlistCount())
                .reviewCount(tourProgramCount.getReviewCount())
                .user(TourProgramUserDto.builder()
                        .id(tourProgram.getUser().getId())
                        .name(tourProgram.getUser().getName())
                        .build())
                .hashtags(hashtags)
                .schedules(tourProgramScheduleDtos)
                .build();

        return responseDto;
    }

    public TourProgramDetailResponseDto updateTourProgram(Long tourProgramId, SaveTourProgramRequestDto requestDto) {

        User user = securityUserService.getUserByJwt();

        TourProgram existTourProgram = tpRepository.findById(tourProgramId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "Tour Program Not Found"));

        if (!existTourProgram.getUser().getId().equals(user.getId())) {
            throw new ApiException(ErrorCode.FORBIDDEN);
        }

        // 썸네일이 수정됐다면 s3에서 삭제
        if (!existTourProgram.getThumbnailUrl().equals(requestDto.getThumbnailUrl())) {
            s3Service.deleteFileByFileName(requestDto.getThumbnailUrl());
        }

        // 필드 업데이트
        existTourProgram.setTitle(requestDto.getTitle());
        existTourProgram.setDescription(requestDto.getDescription());
        existTourProgram.setGuidePrice(requestDto.getGuidePrice());
        existTourProgram.setRegion(requestDto.getRegion());
        existTourProgram.setThumbnailUrl(requestDto.getThumbnailUrl());

        // schedules 업데이트
        tpScheduleRepository.deleteAllByTourProgram(existTourProgram);
        List<TourProgramSchedule> schedules = requestDto.getSchedules().stream()
                .map(scheduleDto -> TourProgramSchedule.builder()
                        .day(scheduleDto.getDay())
                        .scheduleSequence(scheduleDto.getScheduleSequence())
                        .placeName(scheduleDto.getPlaceName())
                        .lat(scheduleDto.getLat())
                        .lon(scheduleDto.getLon())
                        .placeDescription(scheduleDto.getPlaceDescription())
                        .travelTime(scheduleDto.getTravelTime())
                        .tourProgram(existTourProgram)
                        .build())
                .toList();
        tpScheduleRepository.saveAll(schedules);

        // 해시태그 업데이트
        tpHashtagRepository.deleteAllByTourProgram(existTourProgram);
        for (String tag : requestDto.getHashtags()) {
            Hashtag hashtag = hashtagRepository.findByName(tag)
                    .orElseGet(() -> hashtagRepository.save(Hashtag.builder().name(tag).build()));
            existTourProgram.addHashtag(hashtag);
        }
        tpHashtagRepository.saveAll(existTourProgram.getTourProgramHashtags());

        // tourProgram 저장
        tpScheduleRepository.saveAll(schedules);

        // count 객체 get
        TourProgramCount count = tpCountRepository.findByTourProgramId(existTourProgram.getId())
                .orElseThrow(() -> new ApiException(ErrorCode.DATABASE_ERROR, "Tour Program Count Not Found"));

        // 업데이트된 상세 게시물 정보 반환
        return TourProgramDetailResponseDto.builder()
                .TourProgramId(existTourProgram.getId())
                .title(existTourProgram.getTitle())
                .region(existTourProgram.getRegion())
                .description(existTourProgram.getDescription())
                .guidePrice(existTourProgram.getGuidePrice())
                .thumbnailUrl(existTourProgram.getThumbnailUrl())
                .hashtags(existTourProgram.getTourProgramHashtags().stream()
                        .map(h -> h.getHashtag().getName())
                        .toList())
                .schedules(schedules.stream()
                        .map(s -> TourProgramScheduleDto.builder()
                                .day(s.getDay())
                                .scheduleSequence(s.getScheduleSequence())
                                .placeName(s.getPlaceName())
                                .lat(s.getLat())
                                .lon(s.getLon())
                                .placeDescription(s.getPlaceDescription())
                                .travelTime(s.getTravelTime())
                                .build())
                        .toList())
                .user(TourProgramUserDto.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .build())
                .reviewCount(count.getReviewCount())
                .wishlistCount(count.getWishlistCount())
                .build();
    }

    public String deleteTourProgram(Long tourProgramId) {

        User user = securityUserService.getUserByJwt();

        TourProgram existTourProgram = tpRepository.findById(tourProgramId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "Tour Program Not Found"));

        // tourProgram의 작성자가 요청자와 동일하지 않다면 인가 오류
        if (!existTourProgram.getUser().getId().equals(user.getId())) {
            throw new ApiException(ErrorCode.FORBIDDEN);
        }

        // s3에 저장되어 있는 게시물 썸네일이미지 삭제
        s3Service.deleteFile(existTourProgram.getThumbnailUrl());

        // tp와 연결된 스케줄 삭제
        tpScheduleRepository.deleteAllByTourProgram(existTourProgram);

        // tp와 연결된 tourProgramHashtags 삭제
        tpHashtagRepository.deleteAllByTourProgram(existTourProgram);

        // tp와 연결된 count 삭제
        tpCountRepository.deleteByTourProgram(existTourProgram);

        // tp와 연결된 reviews와 그에 대한 Imgs 객체 삭제 로직
        List<Review> reviews = tpReviewRepository.findAllByTourProgramId(existTourProgram.getId());
        for (Review review : reviews) {
            List<Image> reviewImages = imageRepository.findByTargetTypeAndTargetIdOrderBySequenceAsc(ImageTargetType.REVIEW, review.getId());
            for (Image image : reviewImages) {
                // s3에서 이미지 삭제
                s3Service.deleteFileByFileName(image.getImageUrl());
            }
            // DB에서 Imgs 삭제
            imageRepository.deleteAll(reviewImages);
        }
        // DB에서 reviews 삭제
        tpReviewRepository.deleteAll(reviews);

        // tp와 연결된 wishlist 삭제
        tpWishlistRepository.deleteByTourProgram(existTourProgram);

        // tp 삭제
        tpRepository.delete(existTourProgram);
        return "success delete tour program";
    }

    @Transactional(readOnly = true)
    public List<TourProgramDto> getTourProgramList(List<String> hashtags, List<String> regions, int page, int size, String sortOption) {

        if (hashtags.isEmpty() || regions.isEmpty()) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "해시태그 and 지역은 1개 이상 선택해야 합니다.");
        }

        Sort sort = switch (sortOption) {
            case "addedAsc" -> Sort.by("createdAt").ascending();
            case "priceAsc" -> Sort.by("guidePrice").ascending();
            case "priceDesc" -> Sort.by("guidePrice").descending();
            case "reviewDesc" -> Sort.by("reviewCount").descending();
            case "wishlistDesc" -> Sort.by("wishlistCount").descending();
            default -> Sort.by("createdAt").descending();
        };

        Pageable pageable = PageRequest.of(page, size, sort);

        // 동적 필터링 조건 설정: 해시태그 + 지역
        Page<TourProgram> programPage = tpRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 필터링을 위한 JOIN
            Join<TourProgram, TourProgramHashtag> tphJoin = root.join("tourProgramHashtags", JoinType.INNER);
            Join<TourProgramHashtag, Hashtag> hashtagJoin = tphJoin.join("hashtag", JoinType.INNER);

            // 해시태그 조건: hashtag.name IN (:hashtags)
            predicates.add(hashtagJoin.get("name").in(hashtags));
            // JOIN으로 인한 중복 방지
            query.distinct(true);

            // 지역 조건: region IN (:regions)
            predicates.add(root.get("region").in(regions));

            // 모든 조건을 AND로 결합
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        return programPage.stream()
                .map(program -> TourProgramDto.builder()
                        .id(program.getId())
                        .title(program.getTitle())
                        .description(program.getDescription())
                        .guidePrice(program.getGuidePrice())
                        .region(program.getRegion())
                        .thumbnailUrl(program.getThumbnailUrl())
                        .hashtags(program.getTourProgramHashtags().stream()
                                .map(tph -> tph.getHashtag().getName())
                                .toList())
                        .build())
                .toList();
    }
}