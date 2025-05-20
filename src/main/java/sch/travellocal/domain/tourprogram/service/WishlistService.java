package sch.travellocal.domain.tourprogram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sch.travellocal.common.exception.custom.ApiException;
import sch.travellocal.common.exception.error.ErrorCode;
import sch.travellocal.domain.tourprogram.dto.WishlistProgramDto;
import sch.travellocal.domain.tourprogram.entity.TourProgram;
import sch.travellocal.domain.tourprogram.entity.TourProgramCount;
import sch.travellocal.domain.tourprogram.entity.Wishlist;
import sch.travellocal.domain.tourprogram.repository.TourProgramCountRepository;
import sch.travellocal.domain.tourprogram.repository.TourProgramRepository;
import sch.travellocal.domain.tourprogram.repository.WishlistRepository;
import sch.travellocal.domain.user.entity.User;
import sch.travellocal.domain.user.service.SecurityUserService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository tpWishlistRepository;
    private final TourProgramRepository tpRepository;
    private final TourProgramCountRepository tpCountRepository;
    private final SecurityUserService securityUserService;

    public String toggleWishlist(Long tourProgramId) {

        // 현재 로그인한 사용자 정보를 가져와 TravelMbti 엔티티 생성 및 저장
        User user = securityUserService.getUserByJwt();

        TourProgram tourProgram = tpRepository.findById(tourProgramId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "Tour Program not found"));

        TourProgramCount tpCount = tpCountRepository.findByTourProgramId(tourProgramId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "Tour Program count not found"));

        // 유저가 게시물을 위시리스트에 담았는지 유무에 따라 다른 처리
        Optional<Wishlist> existTourProgramWishlist = tpWishlistRepository.findByUserAndTourProgram(user, tourProgram);
        if (existTourProgramWishlist.isPresent()) {
            // 이미 위시리스트였다면 삭제
            tpWishlistRepository.delete(existTourProgramWishlist.get());
            // 위시리스트 수 -1, 존재한다면 개수가 1 이상이기 때문에 예외처리 안함
            tpCount.setWishlistCount(tpCount.getWishlistCount() - 1);
        } else {
            // 위시리스트 추가
            tpWishlistRepository.save(Wishlist.builder()
                    .user(user)
                    .tourProgram(tourProgram)
                    .build()
            );
            // 개수 + 1
            tpCount.setWishlistCount(tpCount.getWishlistCount() + 1);
        }
        // tpCount가 영속상태여서 save 호출 안해도 트렌젝션이 끝나는 시점에 flush 발생해서 update 된다고 예상해서 주석처리
        //tpCountRepository.save(tpCount);

        return "Success Tour_Program Wishlist Toggle Success";
    }

    @Transactional(readOnly = true)
    public List<WishlistProgramDto> getWishlistsByUser(int page, int size, String sortOption) {

        // 현재 로그인한 사용자 정보를 가져와 TravelMbti 엔티티 생성 및 저장
        User user = securityUserService.getUserByJwt();

        // 페이징 정렬 기준
        Sort sort = switch (sortOption) {
            case "addedAsc" -> Sort.by("tourProgram.createdAt").ascending();
            case "priceAsc" -> Sort.by("tourProgram.guidePrice").ascending();
            case "priceDesc" -> Sort.by("tourProgram.guidePrice").descending();
            default -> Sort.by("tourProgram.createdAt").descending();
        };

        // 인터페이스 생성
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<WishlistProgramDto> wishlistPage = tpWishlistRepository.findWishlistProgramsByUserId(user.getId(), pageable);

        return wishlistPage.stream()
                .map(wishlist -> WishlistProgramDto.builder()
                        .tourProgramId(wishlist.getTourProgramId())
                        .thumbnailUrl(wishlist.getThumbnailUrl())
                        .title(wishlist.getTitle())
                        .guidePrice(wishlist.getGuidePrice())
                        .region(wishlist.getRegion())
                        .wishlistCount(wishlist.getWishlistCount())
                        .build())
                .toList();
    }
}