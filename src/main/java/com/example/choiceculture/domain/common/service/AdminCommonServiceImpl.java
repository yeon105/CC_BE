package com.example.choiceculture.domain.common.service;

import com.example.choiceculture.domain.common.dto.CommonInfoDTO;
import com.example.choiceculture.domain.common.entity.CommonInfo;
import com.example.choiceculture.domain.common.repository.CommonInfoRepository;
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

import java.util.List;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class AdminCommonServiceImpl implements AdminCommonService {
    private final CommonInfoRepository commonInfoRepository;

    @Override
    public PageResponseDTO<CommonInfoDTO> getCommon(PageRequestDTO requestDTO) {
        Pageable pageable = PageRequest.of(
                requestDTO.getPage() - 1,
                requestDTO.getSize(),
                Sort.by("id").ascending()
        );

        Page<CommonInfo> commonPage = commonInfoRepository.findAll(pageable);

        List<CommonInfoDTO> dtoList = commonPage.stream().map(info -> CommonInfoDTO.builder()
                .id(info.getId())
                .name(info.getName())
                .useYn(info.getUseYn())
                .build()).toList();

        return PageResponseDTO.<CommonInfoDTO>withAll()
                .dtoList(dtoList)
                .totalCount(commonPage.getTotalElements())
                .pageRequestDTO(requestDTO)
                .build();
    }

    @Override
    public void addCommon(CommonInfoDTO infoDTO) {
        int commonCnt = commonInfoRepository.countById(infoDTO.getId());
        int nextCnt = commonCnt + 1;
        String commonId = infoDTO.getId() + (nextCnt < 10 ? "0" + nextCnt : nextCnt);

        CommonInfo info = CommonInfo.builder()
                .id(commonId)
                .name(infoDTO.getName())
                .useYn(infoDTO.getUseYn())
                .build();
        commonInfoRepository.save(info);
    }

    @Override
    public void editCommon(CommonInfoDTO infoDTO) {
        CommonInfo info = commonInfoRepository.findById(infoDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 카테고리입니다."));

        info.setName(infoDTO.getName());
        info.setUseYn(infoDTO.getUseYn());
        commonInfoRepository.save(info);
    }

    @Override
    public void deleteCommon(String commonId) {
        commonInfoRepository.deleteById(commonId);
    }

}
