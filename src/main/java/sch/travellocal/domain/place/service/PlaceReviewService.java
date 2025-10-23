package sch.travellocal.domain.place.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sch.travellocal.common.exception.custom.ApiException;
import sch.travellocal.common.exception.error.ErrorCode;
import sch.travellocal.domain.place.dto.request.GetPlaceReviewsRequestDto;
import sch.travellocal.domain.place.dto.request.PlaceReviewDto;
import sch.travellocal.domain.place.dto.request.SavePlaceReviewRequestDto;
import sch.travellocal.domain.place.dto.response.AuthorDto;
import sch.travellocal.domain.place.dto.response.PlaceReviewResponseDto;
import sch.travellocal.domain.place.entity.Place;
import sch.travellocal.domain.place.entity.PlaceCount;
import sch.travellocal.domain.place.entity.PlaceReview;
import sch.travellocal.domain.place.repository.PlaceCountRepository;
import sch.travellocal.domain.place.repository.PlaceRepository;
import sch.travellocal.domain.place.repository.PlaceReviewRepository;
import sch.travellocal.domain.place.repository.PlaceUserPermissionRepository;
import sch.travellocal.domain.user.entity.User;
import sch.travellocal.domain.user.service.SecurityUserService;
import sch.travellocal.upload.entity.Image;
import sch.travellocal.upload.enums.ImageTargetType;
import sch.travellocal.upload.repository.ImageRepository;
import sch.travellocal.upload.service.ImageService;
import sch.travellocal.upload.service.S3Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PlaceReviewService {

    private final PlaceReviewRepository placeReviewRepository;
    private final SecurityUserService securityUserService;
    private final PlaceRepository placeRepository;
    private final PlaceCountRepository placeCountRepository;
    private final PlaceUserPermissionRepository placeUserPermissionRepository;
    private final ImageService imageService;
    private final ImageRepository imageRepository;
    private final S3Service s3Service;

    public List<PlaceReviewResponseDto> saveReview(SavePlaceReviewRequestDto request) {

        // user 정보 가져오기 (JWT 토큰에서)
        User user = securityUserService.getUserByJwt();

        // place 존재 유무 검증
        Place place = placeRepository.findByGooglePlaceId(request.getGooglePlaceId())
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "place_id not found"));

        // 장소 리뷰 카운트 존재 유무 검증 및 가져오기
        PlaceCount placeCount = placeCountRepository.findByPlace(place)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "placeCount not found"));

        // 특정 장소에 동일한 유저는 최대 1개까지의 리뷰만 달 수 있음
        if (placeReviewRepository.existsByUserIdAndPlace(user.getId(), place)) {
            throw new ApiException(ErrorCode.DUPLICATE_RESOURCE, "이미 해당 장소에 리뷰를 작성하셨습니다.");
        }

        // 리뷰 저장
        placeReviewRepository.save(
                PlaceReview.builder()
                        .user(user)
                        .place(place)
                        .content(request.getContent())
                        .rating(Float.parseFloat(request.getRating()))
                        .build());

        // 장소 리뷰 카운트, 별점 합 증가 (동시성 고려해야 할 듯)
        placeCount.setReviewCount(placeCount.getReviewCount() + 1);
        placeCount.setSumRating(placeCount.getSumRating() + Float.parseFloat(request.getRating()));

        // 리뷰 업로드 이미지들 순서 보장하여 저장
        imageService.saveImages(ImageTargetType.PLACE_REVIEW, place.getId(), request.getImageUrls());

        // 저장된 이후의 사용자에게 보여질 리뷰 반환
        Pageable pageable = PageRequest.of(0, 5, Sort.by("createdAt").descending());
        Page<PlaceReviewDto> reviewPage = placeReviewRepository.findReviewsByPlaceId(place.getId(), pageable);
        return reviewPage.stream()
                .map(review -> PlaceReviewResponseDto.builder()
                        .author(AuthorDto.builder()
                                .id(user.getId())
                                .name(user.getName())
                                .build())
                        .isAuthor(true)
                        .reviewId(review.getReviewId())
                        .rating(review.getRating())
                        .content(review.getContent())
                        .createdAt(review.getCreatedAt())
                        .updatedAt(review.getUpdatedAt())
                        // n+1 발생, 다만 이미지의 개수가 많지 않기에 큰 문제는 없다고 판단
                        .imagesUrls(imageRepository.findByTargetTypeAndTargetIdOrderBySequenceAsc(ImageTargetType.PLACE_REVIEW, review.getReviewId()).stream()
                                .map(Image::getImageUrl)
                                .toList())
                        .verificationBadge(placeUserPermissionRepository.existsByUserAndPlace(user, place))
                        .build())
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PlaceReviewResponseDto> getReviewsByPlace(String googlePlaceId, GetPlaceReviewsRequestDto request) {

        User user = securityUserService.getUserByJwt();

        Place place = placeRepository.findByGooglePlaceId(googlePlaceId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "Place not found"));

        // 정렬기준 설정
        Sort sort  = switch (request.getSortOption()) {
            case "ratingAsc" -> Sort.by("rating").ascending();
            case "ratingDesc" -> Sort.by("rating").descending();
            case "addedAsc" -> Sort.by("createdAt").ascending();
            default -> Sort.by("createdAt").descending();
        };

        // 인터페이스 생성
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Page<PlaceReviewDto> reviewPage = placeReviewRepository.findReviewsByPlaceId(place.getId(), pageable);

        // 리뷰 정보 반환
        return reviewPage.stream()
                .map(review -> PlaceReviewResponseDto.builder()
                        .author(AuthorDto.builder()
                                .id(review.getAuthorId())
                                .name(review.getAuthorName())
                                .build())
                        .isAuthor(isAuthor(user.getId(), review.getAuthorId()))
                        .reviewId(review.getReviewId())
                        .rating(review.getRating())
                        .content(review.getContent())
                        .createdAt(review.getCreatedAt())
                        .updatedAt(review.getUpdatedAt())
                        // n+1 발생, 다만 이미지의 개수가 많지 않기에 큰 문제는 없다고 판단
                        .imagesUrls(imageRepository.findByTargetTypeAndTargetIdOrderBySequenceAsc(ImageTargetType.PLACE_REVIEW, review.getReviewId()).stream()
                                .map(Image::getImageUrl)
                                .toList())
                        .verificationBadge(placeUserPermissionRepository.existsByUserAndPlace(user, place))
                        .build())
                .toList();
    }

    public String deleteReview(String googlePlaceId, Long reviewId) {

        User user = securityUserService.getUserByJwt();

        // 리뷰 조회
        Place place = placeRepository.findByGooglePlaceId(googlePlaceId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "Place not found"));
        PlaceReview review = placeReviewRepository.findByIdAndPlaceAndUserId(reviewId, place, user.getId())
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "Place Review not found"));

        // 리뷰에 연결된 이미지 삭제
        List<Image> images = imageRepository.findByTargetTypeAndTargetIdOrderBySequenceAsc(ImageTargetType.PLACE_REVIEW, review.getId());
        // S3에서 이미지 삭제
        for (Image image : images) {
            s3Service.deleteFileByFileName(image.getImageUrl());
        }
        // DB에서 이미지 삭제
        imageRepository.deleteAll(images);

        // 장소 리뷰 카운트 감소
        PlaceCount placeCount = placeCountRepository.findByPlace(place)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "Place count not found"));

        placeCount.setReviewCount(placeCount.getReviewCount() - 1);

        // 리뷰 삭제
        placeReviewRepository.delete(review);

        return "success delete review";
    }

    // 내가 작성한 장소에 대한 리뷰 조회 로직
    @Transactional
    public List<PlaceReviewDto> getMyReviews(
            String googlePlaceId,
            int page,
            int size,
            String sortOption
    ) {
        // 로그인한 유저 가져오기
        User user = securityUserService.getUserByJwt();
        if (user == null) {
            throw new ApiException(ErrorCode.FORBIDDEN);
        }

        // 정렬 옵션 처리
        Sort sort = Sort.by("createdAt"); // 기본 정렬
        if ("latest".equals(sortOption)) {
            sort = Sort.by(Sort.Direction.DESC, "createdAt");
        } else if ("oldest".equals(sortOption)) {
            sort = Sort.by(Sort.Direction.ASC, "createdAt");
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        // 장소 + 유저 기준 페이징 조회
        Page<PlaceReview> reviewsPage = placeReviewRepository.findByUserAndGooglePlaceId(user, googlePlaceId, pageable);

        // DTO 변환
        return reviewsPage.stream()
                .map(r -> PlaceReviewDto.builder()
                        .authorId(user.getId())
                        .authorName(user.getName())
                        .reviewId(r.getId())
                        .rating(r.getRating())
                        .content(r.getContent())
                        .createdAt(r.getCreatedAt())
                        .updatedAt(r.getUpdatedAt())
                        .build()
                )
                .collect(Collectors.toList());
    }

    private boolean isAuthor(long currentUserId, long authorId) {
        return currentUserId == authorId;
    }
}
