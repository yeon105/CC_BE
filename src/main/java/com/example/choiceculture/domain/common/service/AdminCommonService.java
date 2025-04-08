package com.example.choiceculture.domain.common.service;

import com.example.choiceculture.domain.common.dto.CommonInfoDTO;
import com.example.choiceculture.dto.PageRequestDTO;
import com.example.choiceculture.dto.PageResponseDTO;

public interface AdminCommonService {
    PageResponseDTO<CommonInfoDTO> getCommon(PageRequestDTO requestDTO);

    void addCommon(CommonInfoDTO infoDTO);

    void editCommon(CommonInfoDTO infoDTO);

    void deleteCommon(String commonId);
}
