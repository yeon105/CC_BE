package com.example.choiceculture.domain.like.service;

import com.example.choiceculture.domain.festival.dto.FestivalInfoDTO;
import com.example.choiceculture.domain.festival.service.FestivalInfoService;
import com.example.choiceculture.domain.like.dto.LikeInfoDTO;
import com.example.choiceculture.domain.festival.entity.FestivalInfo;
import com.example.choiceculture.domain.like.entity.LikeInfo;
import com.example.choiceculture.domain.festival.repository.FestivalInfoRepository;
import com.example.choiceculture.domain.like.repository.LikeInfoRepository;
import com.example.choiceculture.domain.member.entity.Member;
import com.example.choiceculture.domain.member.repository.MemberRepository;
import com.example.choiceculture.dto.PageRequestDTO;
import com.example.choiceculture.dto.PageResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class LikeInfoServiceImpl implements LikeInfoService {
    private final LikeInfoRepository likeInfoRepository;
    private final MemberRepository memberRepository;
    private final FestivalInfoRepository festivalInfoRepository;
    private final FestivalInfoService festivalInfoService;

    @Override
    public String likeOne(LikeInfoDTO infoDTO) {
        Optional<LikeInfo> info = likeInfoRepository.findByDTO(infoDTO.getUserId(), infoDTO.getFestivalId());
        if (info.isPresent()) {
            return "좋아요 설정됨";
        }
        return "좋아요 설정안됨";
    }

    @Override
    public Integer countLike(Integer festivalId) {
        return likeInfoRepository.findByFestivalId(festivalId);
    }

    @Override
    public void addLike(LikeInfoDTO infoDTO) {
        Member member = memberRepository.findById(infoDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        likeInfoRepository.findByDTO(member.getId(), infoDTO.getFestivalId())
                .ifPresent(like -> {
                    throw new IllegalArgumentException("이미 존재합니다.");
                });

        LikeInfo info = LikeInfo.builder()
                .memberId(infoDTO.getUserId())
                .festivalId(infoDTO.getFestivalId())
                .regDate(LocalDateTime.now())
                .build();

        likeInfoRepository.save(info);
    }

    @Override
    public void deleteLike(LikeInfoDTO infoDTO) {
        Member member = memberRepository.findById(infoDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        Optional<LikeInfo> like
                = likeInfoRepository.findByDTO(member.getId(), infoDTO.getFestivalId());
        LikeInfo info = like.get();
        likeInfoRepository.deleteById(info.getId());
    }

    public PageResponseDTO<FestivalInfoDTO> list(PageRequestDTO requestDTO) {
        Pageable pageable = PageRequest.of(
                requestDTO.getPage() - 1,
                requestDTO.getSize(),
                Sort.by("id").ascending()
        );

        Page<LikeInfo> likePage = likeInfoRepository.findByUserId(requestDTO.getUserId(), pageable);

        List<FestivalInfoDTO> dtoList = likePage.stream().map(info -> {
            FestivalInfo festivalInfo = festivalInfoRepository.findById(info.getFestivalId())
                    .orElseThrow(() -> new EntityNotFoundException("좋아하는 공연이 없습니다."));

            return festivalInfoService.entityToDTO(festivalInfo);
        }).toList();

        return PageResponseDTO.<FestivalInfoDTO>withAll()
                .dtoList(dtoList)
                .totalCount(likePage.getTotalElements())
                .pageRequestDTO(requestDTO)
                .build();
    }
}
