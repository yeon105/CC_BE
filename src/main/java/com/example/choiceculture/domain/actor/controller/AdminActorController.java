package com.example.choiceculture.domain.actor.controller;

import com.example.choiceculture.domain.actor.dto.ActorResponseDTO;
import com.example.choiceculture.domain.actor.service.AdminActorService;
import com.example.choiceculture.dto.PageRequestDTO;
import com.example.choiceculture.dto.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/admin/actor")
@RestController
public class AdminActorController {
    private final AdminActorService adminActorService;

    @GetMapping("/list")
    public ResponseEntity<PageResponseDTO<ActorResponseDTO>> getActors(PageRequestDTO requestDTO) {
        PageResponseDTO<ActorResponseDTO> responseDTO = adminActorService.getActors(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addActor(@RequestBody ActorResponseDTO infoDTO) {
        adminActorService.addActor(infoDTO);
        return ResponseEntity.ok().body("배우 추가완료되었습니다.");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteActor(Integer actorId) {
        adminActorService.deleteActor(actorId);
        return ResponseEntity.ok().body("배우 삭제완료되었습니다.");
    }

//    @GetMapping("/search")
//    public ResponseEntity<List<ActorInfoDTO>> findActors(String keyword) {
//        List<ActorInfoDTO> dtoList = adminActorService.findActors(keyword);
//        return ResponseEntity.ok().body(dtoList);
//    }
}
