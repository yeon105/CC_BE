package com.example.choiceculture.domain.ticket.controller;

import com.example.choiceculture.domain.ticket.dto.TicketResponseDTO;
import com.example.choiceculture.domain.ticket.service.AdminTicketService;
import com.example.choiceculture.dto.PageRequestDTO;
import com.example.choiceculture.dto.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/admin/ticket")
@RestController
public class AdminTicketController {
    private final AdminTicketService adminTicketService;

    @GetMapping("/list")
    public ResponseEntity<PageResponseDTO<TicketResponseDTO>> getTickets(PageRequestDTO requestDTO) {
        PageResponseDTO<TicketResponseDTO> dtoList = adminTicketService.getTickets(requestDTO);
        log.info("dtoList: {}", dtoList);
        return ResponseEntity.ok().body(dtoList);
    }

    @PutMapping("/refund")
    public ResponseEntity<String> refund(@RequestParam String orderId, @RequestParam String locationNum) {
        adminTicketService.refund(orderId, locationNum);
        return ResponseEntity.ok().body("환불이 완료되었습니다.");
    }
}
