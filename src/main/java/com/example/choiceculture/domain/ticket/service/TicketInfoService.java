package com.example.choiceculture.domain.ticket.service;

import com.example.choiceculture.domain.ticket.dto.TicketInfoDTO;
import com.example.choiceculture.domain.ticket.dto.TicketResponseDTO;
import com.example.choiceculture.domain.ticket.dto.TicketSeatDTO;
import com.example.choiceculture.dto.PageRequestDTO;
import com.example.choiceculture.dto.PageResponseDTO;

import java.util.List;

public interface TicketInfoService {
    /**
     * 공연ID && 시간대별 선택된 좌석 조회
     * @param seatDTO (festivalId, dateId)
     * @return 선택된 좌석 목록
     */
    List<String> seatList(TicketSeatDTO seatDTO);

    /**
     *  선택한 좌석 추가
     * @param infoDTO (공연ID, 사용자ID, 시간ID, 좌석코드)
     */
    void add(TicketInfoDTO infoDTO);

    /**
     * 좌석선택 취소(삭제)
     * @param ticketId 티켓ID
     */
    void delete(String ticketId);

    PageResponseDTO<TicketResponseDTO> myTickets(PageRequestDTO requestDTO);

    void myRefund(String userId, String orderId, String locationNum);
}
