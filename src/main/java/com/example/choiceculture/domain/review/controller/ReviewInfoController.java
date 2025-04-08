package com.example.choiceculture.domain.review.controller;

import com.example.choiceculture.domain.review.dto.ReviewInfoDTO;
import com.example.choiceculture.domain.review.service.ReviewInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/review")
@RestController
public class ReviewInfoController {
    private final ReviewInfoService reviewInfoService;

    @GetMapping("/list")
    public ResponseEntity<List<ReviewInfoDTO>> list(String type) {
        List<ReviewInfoDTO> dtoList = reviewInfoService.list(type);
        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/my-list")
    public ResponseEntity<List<ReviewInfoDTO>> myList(String userId, String type) {
        List<ReviewInfoDTO> dtoList = reviewInfoService.myList(userId, type);
        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/total-star")
    public ResponseEntity<Map<String, Double>> totalStar(Integer festivalId) {
        double starScore = reviewInfoService.totalStar(festivalId);
        Map<String, Double> starMap = new HashMap<>();
        starMap.put("별점 총점", starScore);
        return ResponseEntity.ok().body(starMap);
    }
}
