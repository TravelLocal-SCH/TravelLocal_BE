package sch.travellocal.domain.travelmbti.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sch.travellocal.domain.travelmbti.dto.base.TravelMbtiDTO;
import sch.travellocal.domain.travelmbti.service.TravelMbtiService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/travel-mbti")
public class TravelMbtiController {

    private final TravelMbtiService travelMbtiService;

    /**
     * 여행 성향 저장 API
     */
    @PostMapping("/mbti")
    public ResponseEntity<?> saveTravelMbti(@RequestBody TravelMbtiDTO travelMbtiDTO) {

        return travelMbtiService.saveTravelMbti(travelMbtiDTO);
    }

    /**
     * 모든 여행 성향 조회 API
     */
    @GetMapping("/all-mbti")
    public ResponseEntity<?> getAllTravelMbti() {

        return travelMbtiService.getAllTravelMbti();
    }

    /**
     * 특정 여행 성향 조회 API
     */
    @GetMapping("/detail-mbti")
    public ResponseEntity<?> getDetailTravelMbti(@RequestParam("mbtiId") Long mbtiId, @RequestParam("mbti") String mbti) {

        return travelMbtiService.getDetailTravelMbti(mbtiId, mbti);
    }

    /**
     * 여행 성향 삭제 API
     */
    @DeleteMapping("/mbti")
    public ResponseEntity<?> deleteTravelMbti(@RequestParam("mbtiId") Long mbtiId, @RequestParam("mbti") String mbti) {

        return travelMbtiService.deleteTravelMbti(mbtiId, mbti);
    }
}
