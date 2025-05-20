package sch.travellocal.domain.tourprogram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sch.travellocal.common.exception.custom.ApiException;
import sch.travellocal.common.exception.error.ErrorCode;
import sch.travellocal.domain.tourprogram.dto.ReviewDto;
import sch.travellocal.domain.tourprogram.dto.UserReviewDto;
import sch.travellocal.domain.tourprogram.dto.request.SaveReviewRequestDto;
import sch.travellocal.domain.tourprogram.dto.response.ReviewResponseDto;
import sch.travellocal.domain.tourprogram.dto.response.UserReviewResponseDto;
import sch.travellocal.domain.tourprogram.entity.TourProgramCount;
import sch.travellocal.domain.tourprogram.entity.Review;
import sch.travellocal.domain.tourprogram.entity.TourProgram;
import sch.travellocal.domain.tourprogram.repository.TourProgramCountRepository;
import sch.travellocal.domain.tourprogram.repository.ReviewRepository;
import sch.travellocal.domain.tourprogram.repository.TourProgramRepository;
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
public class ReviewService {

    private final ReviewRepository tpReviewRepository;
    private final TourProgramRepository tpRepository;
    private final TourProgramCountRepository tpCountRepository;
    private final ImageRepository imageRepository;
    private final SecurityUserService securityUserService;
    private final S3Service s3Service;

    @Value("{aws.url}")
    private String downloadUrl;

    public String saveReview(SaveReviewRequestDto request) {

        User user = securityUserService.getUserByJwt();

        TourProgram tourProgram = tpRepository.findById(request.getTourProgramId())
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "Tour Program not found"));

        TourProgramCount tpCount = tpCountRepository.findByTourProgramId(request.getTourProgramId())
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "Tour Program count not found"));

        // 특정 게시물에 동일한 유저는 최대 1개까지의 리뷰만 달 수 있음
        if (tpReviewRepository.existsByTourProgramAndUser(tourProgram, user)) {
            throw new ApiException(ErrorCode.DUPLICATE_RESOURCE, "이미 해당 게시물에 리뷰를 작성하셨습니다.");
        }

        Review review = Review.builder()
                .rating(Float.parseFloat(request.getRating()))
                .content(request.getContent())
                .user(user)
                .tourProgram(tourProgram)
                .build();

        // 저장하고 해당 객체 사용하기 위해 반환받음
        review = tpReviewRepository.save(review);

        tpCount.setReviewCount(tpCount.getReviewCount() + 1);
        //tpCountRepository.save(tpCount);

        // 리뷰 업로드 이미지들 순서 보장하여 저장
        if (request.getImageUrls() != null) {
            List<Image> images = new ArrayList<>();
            int seq = 0;
            for (String url : request.getImageUrls()) {
                images.add(Image.builder()
                        .imageUrl(url)
                        .sequence(seq++)
                        .targetType(ImageTargetType.REVIEW)
                        .targetId(review.getId())
                        .build());
            }
            imageRepository.saveAll(images);
        }
        return "success save review";
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getReviewsByTourProgram(Long tourProgramId, int page, int size, String sortOption) {

        if (!tpRepository.existsById(tourProgramId)) {
            throw new ApiException(ErrorCode.NOT_FOUND, "Tour Program not found");
        }

        // 정렬기준 설정
        Sort sort  = switch (sortOption) {
            case "ratingAsc" -> Sort.by("rating").ascending();
            case "ratingDesc" -> Sort.by("rating").descending();
            default -> Sort.by("createdAt").descending();
        };

        // 인터페이스 생성
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ReviewDto> reviewPage = tpReviewRepository.findReviewsByTourProgramId(tourProgramId, pageable);

        // 리뷰 정보 반환
        return reviewPage.stream()
                .map(review -> ReviewResponseDto.builder()
                        .userId(review.getUserId())
                        .name(review.getUserName())
                        .rating(review.getRating())
                        .content(review.getContent())
                        .createdAt(review.getCreatedAt())
                        .updatedAt(review.getUpdatedAt())
                        // n+1 발생, 다만 이미지의 개수가 많지 않기에 큰 문제는 없다고 판단
                        .imagesUrls(imageRepository.findByTargetTypeAndTargetIdOrderBySequenceAsc(ImageTargetType.REVIEW, review.getReviewId()).stream()
                                .map(Image::getImageUrl)
                                .toList())
                        .build())
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UserReviewResponseDto> getReviewsByUser(int page, int size, String sortOption) {

        User user = securityUserService.getUserByJwt();

        // 정렬기준 설정
        Sort sort  = switch (sortOption) {
            case "ratingAsc" -> Sort.by("rating").ascending();
            case "ratingDesc" -> Sort.by("rating").descending();
            default -> Sort.by("createdAt").descending();
        };

        // 인터페이스 생성
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<UserReviewDto> reviewPage = tpReviewRepository.findReviewsByUserId(user.getId(), pageable);

        // 리뷰 정보 반환
        return reviewPage.stream()
                .map(review -> UserReviewResponseDto.builder()
                        .tourProgramId(review.getTourProgramId())
                        .title(review.getTitle())
                        .rating(review.getRating())
                        .content(review.getContent())
                        .createdAt(review.getCreatedAt())
                        .updatedAt(review.getUpdatedAt())
                        .imagesUrls(imageRepository.findByTargetTypeAndTargetIdOrderBySequenceAsc(ImageTargetType.REVIEW, review.getReviewId()).stream()
                                .map(Image::getImageUrl)
                                .toList())
                        .build())
                .toList();
    }

    public String deleteReview(Long tourProgramId) {

        User user = securityUserService.getUserByJwt();

        // 리뷰 조회
        Review review = tpReviewRepository.findByTourProgramIdAndUserId(tourProgramId, user.getId())
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "TourProgram Review not found"));

        // 리뷰에 연결된 이미지 삭제
        List<Image> images = imageRepository.findByTargetTypeAndTargetIdOrderBySequenceAsc(ImageTargetType.REVIEW, review.getId());
        // S3에서 이미지 삭제
        for (Image image : images) {
            s3Service.deleteFileByFileName(image.getImageUrl());
        }
        // DB에서 이미지 삭제
        imageRepository.deleteAll(images);

        TourProgramCount tpCount = tpCountRepository.findByTourProgramId(tourProgramId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "Tour Program count not found"));

        tpCount.setReviewCount(tpCount.getReviewCount() - 1);
        //tpCountRepository.save(tpCount);

        // 리뷰 삭제
        tpReviewRepository.delete(review);
        return "success delete review";
    }
}
