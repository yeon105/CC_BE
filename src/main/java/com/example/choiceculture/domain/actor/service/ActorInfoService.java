package com.example.choiceculture.domain.actor.service;

import com.example.choiceculture.domain.actor.dto.ActorResponseDTO;
import com.example.choiceculture.domain.actor.dto.ActorInfoDTO;
import com.example.choiceculture.domain.actor.entity.ActorInfo;

import java.util.List;

public interface ActorInfoService {
    /**
     * 캐스팅 정보 조회
     * @param festivalId 공연ID
     * @return 공연 캐스팅 등장인물 목록
     */
    List<ActorInfoDTO> castingList(Integer festivalId);

    // entity -> dto 변환
    default ActorInfoDTO entityToDTO(ActorInfo info) {
        return ActorInfoDTO.builder()
                .id(info.getId())
                .actorCharacter(info.getActorCharacter())
                .actorName(info.getActorName())
                .profileImage(info.getActorImageUrl())
                .build();
    }

    // dto -> entity 변환
    default ActorInfo dtoToEntity(ActorResponseDTO infoDTO) {
        return ActorInfo.builder()
                .festivalId(infoDTO.getFestivalId())
                .actorCharacter(infoDTO.getActorCharacter())
                .actorName(infoDTO.getActorName())
                .actorImageUrl(infoDTO.getActorImageUrl())
                .build();
    }
}
