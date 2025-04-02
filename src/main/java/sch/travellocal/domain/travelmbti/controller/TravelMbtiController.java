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

    @PostMapping
    public ResponseEntity<?> saveTravelMbti(@RequestBody TravelMbtiDTO travelMbtiDTO) {

        return travelMbtiService.saveTravelMbti(travelMbtiDTO);
    }

    @GetMapping("/all-mbti")
    public ResponseEntity<?> getAllTravelMbti() {

        return travelMbtiService.getAllTravelMbti();
    }

    @GetMapping("/detail-mbti")
    public ResponseEntity<?> getDetailTravelMbti(@RequestParam("mbtiId") Long mbtiId, @RequestParam("mbti") String mbti) {

        return travelMbtiService.getDetailTravelMbti(mbtiId, mbti);
    }
}
