package com.example.choiceculture.domain.ticket.repository.querydsl;

import com.example.choiceculture.domain.ticket.dto.TicketResponseDTO;
import com.example.choiceculture.dto.PageRequestDTO;
import org.springframework.data.domain.Page;

public interface TicketInfoRepositoryCustom {
    Page<TicketResponseDTO> getTickets(PageRequestDTO requestDTO);

}
