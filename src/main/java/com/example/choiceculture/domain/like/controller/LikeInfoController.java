package com.example.choiceculture.domain.like.controller;

import com.example.choiceculture.domain.festival.dto.FestivalInfoDTO;
import com.example.choiceculture.domain.like.dto.LikeInfoDTO;
import com.example.choiceculture.domain.like.service.LikeInfoService;
import com.example.choiceculture.dto.PageRequestDTO;
import com.example.choiceculture.dto.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/like")
@RestController
public class LikeInfoController {
    private final LikeInfoService likeInfoService;

    @GetMapping("/one")
    public ResponseEntity<String> likeOne(LikeInfoDTO infoDTO) {
        String comment = likeInfoService.likeOne(infoDTO);
        return ResponseEntity.ok().body(comment);
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Integer>> countLike(Integer festivalId) {
        Integer cntLike = likeInfoService.countLike(festivalId);
        Map<String, Integer> cntMap = new HashMap<>();
        cntMap.put("좋아요 개수", cntLike);
        return ResponseEntity.ok().body(cntMap);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addLike(LikeInfoDTO infoDTO) {
        likeInfoService.addLike(infoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("좋아요 추가완료");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteLike(LikeInfoDTO infoDTO) {
        likeInfoService.deleteLike(infoDTO);
        return ResponseEntity.ok().body("좋아요 삭제완료");
    }

    @GetMapping("/user")
    public ResponseEntity<PageResponseDTO<FestivalInfoDTO>> list(PageRequestDTO requestDTO) {
        PageResponseDTO<FestivalInfoDTO> dtoList = likeInfoService.list(requestDTO);
        return ResponseEntity.ok().body(dtoList);
    }

}
