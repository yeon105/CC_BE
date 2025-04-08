package com.example.choiceculture.domain.festival.controller;

import com.example.choiceculture.domain.actor.dto.ActorInfoDTO;
import com.example.choiceculture.domain.festival.dto.*;
import com.example.choiceculture.domain.actor.service.ActorInfoService;
import com.example.choiceculture.domain.festival.service.FestivalInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/festival")
@RestController
public class FestivalInfoController {
    private final FestivalInfoService festivalInfoService;
    private final ActorInfoService actorInfoService;

    @GetMapping("/ticket-open")
    public ResponseEntity<List<FestivalInfoDTO>> openTicket() {
        List<FestivalInfoDTO> dtoList = festivalInfoService.openTicket();
        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/detail")
    public ResponseEntity<FestivalInfoDTO> getOne(Integer festivalId) {
        FestivalInfoDTO dto = festivalInfoService.getOne(festivalId);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/casting-list")
    public ResponseEntity<List<ActorInfoDTO>> castingList(Integer festivalId) {
        List<ActorInfoDTO> dtoList = actorInfoService.castingList(festivalId);
        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/list")
    public ResponseEntity<List<FestivalInfoDTO>> list(FestivalRequestDTO requestDTO) {
        List<FestivalInfoDTO> dtoList = festivalInfoService.list(requestDTO);
        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/list/category")
    public ResponseEntity<List<FestivalInfoDTO>> listCategory(FestivalRequestDTO requestDTO) {
        List<FestivalInfoDTO> dtoList = festivalInfoService.listCategory(requestDTO);
        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/search")
    public ResponseEntity<SearchResponseDTO> search(String searchKeyword) {
        SearchResponseDTO dtoList = festivalInfoService.search(searchKeyword);
        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<FestivalInfoDTO>> rankingList() {
        List<FestivalInfoDTO> dtoList = festivalInfoService.rankingList();
        return ResponseEntity.ok().body(dtoList);
    }
    @GetMapping("/ranking-limit")
    public ResponseEntity<List<FestivalInfoDTO>> rankingLimitList(int limit) {
        List<FestivalInfoDTO> dtoList = festivalInfoService.LimitRanking(limit);
        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/favorite-ranking")
    public ResponseEntity<List<FestivalInfoDTO>> favoriteRanking(String userId) {
        List<FestivalInfoDTO> dtoList = festivalInfoService.favoriteRanking(userId);
        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/favorite/ranking-limit")
    public ResponseEntity<List<FestivalInfoDTO>> favoriteLimit(String userId) {
        List<FestivalInfoDTO> dtoList = festivalInfoService.favoriteLimit(userId);
        return ResponseEntity.ok().body(dtoList);
    }

    @PostMapping(value = "/add", consumes = "multipart/form-data")
    public ResponseEntity<String> addFestival(FestivalAddDTO infoDTO) {
        festivalInfoService.addFestival(infoDTO);
        return ResponseEntity.ok().body("공연 추가완료되었습니다.");
    }
}
