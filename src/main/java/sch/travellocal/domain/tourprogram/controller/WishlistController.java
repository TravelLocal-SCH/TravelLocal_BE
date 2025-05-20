package sch.travellocal.domain.tourprogram.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sch.travellocal.common.response.SuccessResponse;
import sch.travellocal.domain.tourprogram.dto.WishlistProgramDto;
import sch.travellocal.domain.tourprogram.dto.request.GetWishlistsRequestDto;
import sch.travellocal.domain.tourprogram.service.WishlistService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
@Tag(name = "TourProgram_Wishlist", description = "투어 프로그램에 대한 위시리스트 API")
public class WishlistController {

    private final WishlistService tpWishlistService;

    /**
     * 위시리스트 저장/삭제 API
     * front측에서 유저의 게시물에 대한 위시리스트 유무를 파악하기보단 토글형태로 이미 존재한다면 위시리스트에서 삭제, 존재하지 않는다면 추가하도록 하는 로직이 적절하다 판단
     */
    @PostMapping("/{tourProgramId}")
    @Operation(
            summary = "투어 프로그램 id를 통한 위시리스트 추가/삭제",
            description = "토글형태로 사용되는 API입니다. 이미 추가됐던 투어 프로그램이라면 위시리스트에서 삭제, 추가되지 않았다면 추가하는 동작을 수행합니다.\n" +
                    "(유저에겐 위시리스트(하트버튼)이 클릭될 때마다 토글되는 단순 UI만 적용해주면 됨, 추가적인 API 요청 필요 X)"
    )
    public ResponseEntity<SuccessResponse<String>> toggleWishlist(@PathVariable Long tourProgramId) {

        return ResponseEntity.ok(SuccessResponse.ok(tpWishlistService.toggleWishlist(tourProgramId)));
    }

    /**
     * 나의 전체 위시리스트 조회 API (메인화면에서 위시리스트 버튼 클릭 시)
     */
    @GetMapping
    @Operation(
            summary = "유저의 위시리스트 조회",
            description = "요청에서 JWT를 통해 요청자가 추가한 위시리스트를 응답으로 제공합니다"
    )
    public ResponseEntity<List<WishlistProgramDto>> getWishlistsByUser(@Valid @ModelAttribute GetWishlistsRequestDto requestDto) {

        return ResponseEntity.ok(tpWishlistService.getWishlistsByUser(
                requestDto.getPage(),
                requestDto.getSize(),
                requestDto.getSortOption()
        ));
    }
}