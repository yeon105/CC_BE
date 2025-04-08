package com.example.choiceculture.domain.actor.repository.querydsl;

import com.example.choiceculture.domain.actor.dto.ActorResponseDTO;
import com.example.choiceculture.dto.PageRequestDTO;
import org.springframework.data.domain.Page;

public interface ActorInfoRepositoryCustom {
    Page<ActorResponseDTO> getActors(PageRequestDTO requestDTO);

}
