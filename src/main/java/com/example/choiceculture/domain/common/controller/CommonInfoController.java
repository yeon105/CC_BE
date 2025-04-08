package com.example.choiceculture.domain.common.controller;

import com.example.choiceculture.domain.common.dto.CommonRequestDTO;
import com.example.choiceculture.domain.common.service.CommonInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/common")
@RestController
public class CommonInfoController {
    private final CommonInfoService commonInfoService;

    @GetMapping("/list")
    public ResponseEntity<List<CommonRequestDTO>> list(String id) {
        List<CommonRequestDTO> dtoList = commonInfoService.list(id);
        return ResponseEntity.ok().body(dtoList);
    }
    @GetMapping("/list/category")
    public ResponseEntity<List<CommonRequestDTO>> listCategory(String id) {
        List<CommonRequestDTO> dtoList = commonInfoService.listCategory(id);
        return ResponseEntity.ok().body(dtoList);
    }

}
