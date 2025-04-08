package com.example.choiceculture.domain.festival.service;

import com.example.choiceculture.domain.festival.dto.FestivalInfoAccessDTO;
import com.example.choiceculture.domain.festival.dto.FestivalInfoDTO;
import com.example.choiceculture.domain.festival.dto.FestivalResponseDTO;
import com.example.choiceculture.dto.PageRequestDTO;
import com.example.choiceculture.dto.PageResponseDTO;

import java.util.List;

public interface AdminFestivalService {

    PageResponseDTO<FestivalInfoDTO> getFestivals(PageRequestDTO requestDTO);

    void deleteFestival(Integer festivalId);

    List<FestivalResponseDTO> getIdList();

//    List<FestivalInfoDTO> findFestivals(String keyword);

    PageResponseDTO<FestivalInfoAccessDTO> applyList(PageRequestDTO requestDTO);

    void register(Integer accessId, Integer festivalId);

    void refusal(Integer festivalId);

}
