package sch.travellocal.domain.place.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
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

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional // 기본적으로 쓰기 트랜잭션 적용
public class PlaceReviewService {

    private final PlaceReviewRepository placeReviewRepository;
    private final SecurityUserService securityUserService;
    private final PlaceRepository placeRepository;
    private final PlaceCountRepository placeCountRepository;
    private final PlaceUserPermissionRepository placeUserPermissionRepository;
    private final ImageService imageService;
    private final ImageRepository imageRepository;
    private final S3Service s3Service;

    @Caching(evict = {
            @CacheEvict(value = "placeDetail", key = "#request.googlePlaceId + '_kor'"),
            @CacheEvict(value = "placeDetail", key = "#request.googlePlaceId + '_eng'"),
            @CacheEvict(value = "placeDetail", key = "#request.googlePlaceId + '_jpn'")
    })
    public List<PlaceReviewResponseDto> saveReview(SavePlaceReviewRequestDto request) {
        User user = securityUserService.getUserByJwt();

        Place place = placeRepository.findByGooglePlaceId(request.getGooglePlaceId())
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "place_id not found"));

        PlaceCount placeCount = placeCountRepository.findByPlace(place)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "placeCount not found"));

        if (placeReviewRepository.existsByUserIdAndPlace(user.getId(), place)) {
            throw new ApiException(ErrorCode.DUPLICATE_RESOURCE, "이미 해당 장소에 리뷰를 작성하셨습니다.");
        }

        float rating = Float.parseFloat(request.getRating());

        PlaceReview savedReview = placeReviewRepository.save(
                PlaceReview.builder()
                        .user(user)
                        .place(place)
                        .content(request.getContent())
                        .rating(rating)
                        .build());

        placeCount.setReviewCount(placeCount.getReviewCount() + 1);
        placeCount.setSumRating(placeCount.getSumRating() + rating);

        imageService.saveImages(ImageTargetType.PLACE_REVIEW, savedReview.getId(), request.getImageUrls());

        Pageable pageable = PageRequest.of(0, 5, Sort.by("createdAt").descending());
        Page<PlaceReviewDto> reviewPage = placeReviewRepository.findReviewsByPlaceId(place.getId(), pageable);

        return convertToResponseDtos(reviewPage.getContent(), place.getId(), user.getId());
    }

    @Transactional(readOnly = true)
    public List<PlaceReviewResponseDto> getReviewsByPlace(String googlePlaceId, GetPlaceReviewsRequestDto request) {
        User user = securityUserService.getUserByJwt();

        Place place = placeRepository.findByGooglePlaceId(googlePlaceId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "Place not found"));

        Sort sort = switch (request.getSortOption()) {
            case "ratingAsc" -> Sort.by("rating").ascending();
            case "ratingDesc" -> Sort.by("rating").descending();
            case "addedAsc" -> Sort.by("createdAt").ascending();
            default -> Sort.by("createdAt").descending();
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        Page<PlaceReviewDto> reviewPage = placeReviewRepository.findReviewsByPlaceId(place.getId(), pageable);

        return convertToResponseDtos(reviewPage.getContent(), place.getId(), user != null ? user.getId() : -1L);
    }

    @Caching(evict = {
            @CacheEvict(value = "placeDetail", key = "#googlePlaceId + '_kor'"),
            @CacheEvict(value = "placeDetail", key = "#googlePlaceId + '_eng'"),
            @CacheEvict(value = "placeDetail", key = "#googlePlaceId + '_jpn'")
    })
    public String deleteReview(String googlePlaceId, Long reviewId) {
        User user = securityUserService.getUserByJwt();

        Place place = placeRepository.findByGooglePlaceId(googlePlaceId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "Place not found"));

        PlaceReview review = placeReviewRepository.findByIdAndPlaceAndUserId(reviewId, place, user.getId())
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "Place Review not found"));

        // 이미지 삭제 (S3 + DB)
        List<Image> images = imageRepository.findByTargetTypeAndTargetIdOrderBySequenceAsc(ImageTargetType.PLACE_REVIEW, review.getId());
        images.forEach(image -> s3Service.deleteFileByFileName(image.getImageUrl()));
        imageRepository.deleteAll(images);

        // 통계 감소 (리뷰 개수 및 평점 합계 차감)
        PlaceCount placeCount = placeCountRepository.findByPlace(place)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "Place count not found"));
        placeCount.setReviewCount(Math.max(0, placeCount.getReviewCount() - 1));
        placeCount.setSumRating(Math.max(0, placeCount.getSumRating() - review.getRating()));

        placeReviewRepository.delete(review);
        return "success delete review";
    }

    @Transactional(readOnly = true)
    public List<PlaceReviewDto> getMyReviews(int page, int size, String sortOption) {
        User user = securityUserService.getUserByJwt();
        if (user == null) throw new ApiException(ErrorCode.FORBIDDEN);

        Sort sort = "oldest".equals(sortOption) ? Sort.by(Sort.Direction.ASC, "createdAt") : Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<PlaceReview> reviewsPage = placeReviewRepository.findByUser(user, pageable);

        return reviewsPage.stream()
                .map(r -> PlaceReviewDto.builder()
                        .authorId(user.getId())
                        .authorName(user.getName())
                        .reviewId(r.getId())
                        .rating(r.getRating())
                        .content(r.getContent())
                        .createdAt(r.getCreatedAt())
                        .updatedAt(r.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    private List<PlaceReviewResponseDto> convertToResponseDtos(List<PlaceReviewDto> reviewDtos, Long placeId, Long currentUserId) {
        if (reviewDtos.isEmpty()) return Collections.emptyList();

        List<Long> reviewIds = reviewDtos.stream().map(PlaceReviewDto::getReviewId).toList();
        List<Long> authorIds = reviewDtos.stream().map(PlaceReviewDto::getAuthorId).distinct().toList();

        Map<Long, List<String>> imageMap = imageRepository.findByTargetTypeAndTargetIdIn(
                        ImageTargetType.PLACE_REVIEW, reviewIds)
                .stream()
                .collect(Collectors.groupingBy(Image::getTargetId,
                        Collectors.mapping(Image::getImageUrl, Collectors.toList())));

        Set<Long> verifiedAuthorIds = new HashSet<>(
                placeUserPermissionRepository.findUserIdsWithPermission(placeId, authorIds)
        );

        return reviewDtos.stream()
                .map(review -> PlaceReviewResponseDto.builder()
                        .author(AuthorDto.builder()
                                .id(review.getAuthorId())
                                .name(review.getAuthorName())
                                .build())
                        .isAuthor(currentUserId.equals(review.getAuthorId()))
                        .reviewId(review.getReviewId())
                        .rating(review.getRating())
                        .content(review.getContent())
                        .createdAt(review.getCreatedAt())
                        .updatedAt(review.getUpdatedAt())
                        .imagesUrls(imageMap.getOrDefault(review.getReviewId(), Collections.emptyList()))
                        .verificationBadge(verifiedAuthorIds.contains(review.getAuthorId()))
                        .build())
                .toList();
    }
}
