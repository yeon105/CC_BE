package com.example.choiceculture.domain.actor.service;

import com.example.choiceculture.domain.actor.dto.ActorResponseDTO;
import com.example.choiceculture.dto.PageRequestDTO;
import com.example.choiceculture.dto.PageResponseDTO;

public interface AdminActorService {

    PageResponseDTO<ActorResponseDTO> getActors(PageRequestDTO requestDTO);

    void addActor(ActorResponseDTO infoDTO);

    void deleteActor(Integer actorId);

//    List<ActorInfoDTO> findActors(String keyword);
}
