package com.example.choiceculture.domain.actor.service;

import com.example.choiceculture.domain.actor.dto.ActorInfoDTO;
import com.example.choiceculture.domain.actor.entity.ActorInfo;
import com.example.choiceculture.domain.actor.repository.ActorInfoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ActorInfoServiceImpl implements ActorInfoService {
    private final ActorInfoRepository actorInfoRepository;

    @Override
    public List<ActorInfoDTO> castingList(Integer festivalId) {
        List<ActorInfo> infoList = actorInfoRepository.findByFestivalId(festivalId);
        if (infoList.isEmpty()) {
            throw new EntityNotFoundException("해당 등장인물이 존재하지 않습니다. festivalId: " + festivalId);
        }
        return infoList.stream().map(this::entityToDTO).toList();
    }
}
