package com.example.choiceculture.domain.review.service;

import com.example.choiceculture.domain.review.dto.ReviewInfoDTO;
import com.example.choiceculture.domain.review.entity.ReviewInfo;
import com.example.choiceculture.domain.festival.enums.ReviewType;
import com.example.choiceculture.domain.festival.repository.FestivalInfoRepository;
import com.example.choiceculture.domain.review.repository.ReviewInfoRepository;
import com.example.choiceculture.domain.member.entity.Member;
import com.example.choiceculture.domain.member.repository.MemberRepository;
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
public class ReviewInfoServiceImpl implements ReviewInfoService {
    private final ReviewInfoRepository reviewInfoRepository;
    private final MemberRepository memberRepository;
    private final FestivalInfoRepository festivalInfoRepository;

    @Override
    public List<ReviewInfoDTO> list(String type) {
        if (!type.equals("REVIEW") && !type.equals("HOPE") && !type.equals("QA")) {
            throw new IllegalArgumentException("잘못된 값입니다: " + type);
        }

        List<ReviewInfo> infoList = reviewInfoRepository.findByType(ReviewType.valueOf(type));
        if (infoList.isEmpty()) {
            throw new EntityNotFoundException("해당 정보가 없습니다. type: " + type);
        }
        return infoList.stream().map(this::entityToDTO).toList();
    }

    @Override
    public List<ReviewInfoDTO> myList(String userId, String type) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        if (!type.equals("REVIEW") && !type.equals("HOPE") && !type.equals("QA")) {
            throw new IllegalArgumentException("잘못된 값입니다: " + type);
        }

        List<ReviewInfo> infoList = reviewInfoRepository.findTypeByMemberId(member.getId(), ReviewType.valueOf(type.toUpperCase()));
        if (infoList.isEmpty()) {
            throw new EntityNotFoundException("해당 정보가 없습니다. type: " + type);
        }
        return infoList.stream().map(this::entityToDTO).toList();
    }

    @Override
    public double totalStar(Integer festivalId) {
        Double averageRating = reviewInfoRepository.findAverageRating(festivalId);
        if (averageRating == null) {
            return 0.0;
        }
        return Math.round(averageRating * 10) / 10.0;
    }

}
