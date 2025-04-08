package com.example.choiceculture.domain.festival.dto;

import com.example.choiceculture.domain.actor.dto.ActorInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SearchResponseDTO {
    private List<FestivalInfoDTO> festivalResults;
    private List<ActorInfoDTO> actorResults;
}
