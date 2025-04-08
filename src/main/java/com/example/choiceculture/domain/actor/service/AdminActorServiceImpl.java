package com.example.choiceculture.domain.actor.service;

import com.example.choiceculture.domain.actor.dto.ActorResponseDTO;
import com.example.choiceculture.domain.actor.entity.ActorInfo;
import com.example.choiceculture.domain.actor.repository.ActorInfoRepository;
import com.example.choiceculture.domain.festival.repository.FestivalInfoRepository;
import com.example.choiceculture.dto.PageRequestDTO;
import com.example.choiceculture.dto.PageResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class AdminActorServiceImpl implements AdminActorService {
    private final ActorInfoService actorInfoService;
    private final FestivalInfoRepository festivalInfoRepository;
    private final ActorInfoRepository actorInfoRepository;
    @Override
    public PageResponseDTO<ActorResponseDTO> getActors(PageRequestDTO requestDTO) {
        Page<ActorResponseDTO> pageResult = actorInfoRepository.getActors(requestDTO);

        return PageResponseDTO.<ActorResponseDTO>withAll()
                .dtoList(pageResult.getContent())
                .totalCount(pageResult.getTotalElements())
                .pageRequestDTO(requestDTO)
                .build();
    }

    @Override
    public void addActor(ActorResponseDTO infoDTO) {
        festivalInfoRepository.findById(infoDTO.getFestivalId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 공연입니다."));

        ActorInfo actorInfo = actorInfoService.dtoToEntity(infoDTO);
        actorInfoRepository.save(actorInfo);
    }

    @Override
    public void deleteActor(Integer actorId) {
        actorInfoRepository.deleteById(actorId);
    }

//    @Override
//    public List<ActorInfoDTO> findActors(String keyword) {
//        List<ActorInfo> infoList = actorInfoRepository.findByActorKeyword(keyword);
//        return infoList.stream().map(actorInfoService::entityToDTO).toList();
//    }
}
