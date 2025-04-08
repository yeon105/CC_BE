package com.example.choiceculture.domain.ticket.service;

import com.example.choiceculture.domain.ticket.dto.TicketResponseDTO;
import com.example.choiceculture.dto.PageRequestDTO;
import com.example.choiceculture.dto.PageResponseDTO;

public interface AdminTicketService {
    PageResponseDTO<TicketResponseDTO> getTickets(PageRequestDTO requestDTO);

    void refund(String orderId, String locationNum);
}
