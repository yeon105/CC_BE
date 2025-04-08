package com.example.choiceculture.domain.ticket.controller;

import com.example.choiceculture.domain.ticket.dto.TicketInfoDTO;
import com.example.choiceculture.domain.ticket.dto.TicketResponseDTO;
import com.example.choiceculture.domain.ticket.dto.TicketSeatDTO;
import com.example.choiceculture.domain.ticket.service.TicketInfoService;
import com.example.choiceculture.dto.PageRequestDTO;
import com.example.choiceculture.dto.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/ticket")
@RestController
public class TicketInfoController {
    private final TicketInfoService ticketInfoService;

    @GetMapping("/seat")
    public ResponseEntity<List<String>> seatList(TicketSeatDTO seatDTO) {
        List<String> dtoList = ticketInfoService.seatList(seatDTO);
        return ResponseEntity.ok().body(dtoList);
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody TicketInfoDTO infoDTO) {
        ticketInfoService.add(infoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("좌석선택이 완료되었습니다.");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(String ticketId) {
        ticketInfoService.delete(ticketId);
        return ResponseEntity.ok().body("좌석선택이 취소되었습니다.");
    }

    @GetMapping("/list")
    public ResponseEntity<PageResponseDTO<TicketResponseDTO>> myTickets(PageRequestDTO requestDTO) {
        PageResponseDTO<TicketResponseDTO> dtoList = ticketInfoService.myTickets(requestDTO);
        log.info("dtoList: {}", dtoList);
        return ResponseEntity.ok().body(dtoList);
    }

    @PutMapping("/refund")
    public ResponseEntity<String> myRefund(@RequestParam String userId, @RequestParam String orderId,
                                         @RequestParam String locationNum) {
        ticketInfoService.myRefund(userId, orderId, locationNum);
        return ResponseEntity.ok().body("환불신청이 완료되었습니다.");
    }

}
