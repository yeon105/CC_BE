package com.example.choiceculture.domain.ticket.service;

import com.example.choiceculture.domain.ticket.dto.TicketResponseDTO;
import com.example.choiceculture.domain.festival.enums.ReFundState;
import com.example.choiceculture.domain.ticket.repository.TicketInfoRepository;
import com.example.choiceculture.dto.PageRequestDTO;
import com.example.choiceculture.dto.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class AdminTicketServiceImpl implements AdminTicketService {
    private final TicketInfoRepository ticketInfoRepository;

    @Override
    public PageResponseDTO<TicketResponseDTO> getTickets(PageRequestDTO requestDTO) {
        Page<TicketResponseDTO> pageResult = ticketInfoRepository.getTickets(requestDTO);
        pageResult.forEach(ticket -> ticket.setRefundStateName(ticket.getRefundState().getDescription()));

        return PageResponseDTO.<TicketResponseDTO>withAll()
                .dtoList(pageResult.getContent())
                .totalCount(pageResult.getTotalElements())
                .pageRequestDTO(requestDTO)
                .build();
    }

    @Override
    public void refund(String orderId, String locationNum) {
        String[] seats = locationNum.split(", ");
        for (String seat : seats) {
//            log.info("orderId: {}", orderId + '-' + seat);
            ticketInfoRepository.findById(orderId + '-' + seat).ifPresent(ticket -> {
                ticket.setRefundState(ReFundState.COMPLETED);
                ticketInfoRepository.save(ticket);
            });
        }
    }

}
