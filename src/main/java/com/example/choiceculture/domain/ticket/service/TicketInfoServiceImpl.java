package com.example.choiceculture.domain.ticket.service;

import com.example.choiceculture.domain.ticket.dto.TicketInfoDTO;
import com.example.choiceculture.domain.ticket.dto.TicketResponseDTO;
import com.example.choiceculture.domain.ticket.dto.TicketSeatDTO;
import com.example.choiceculture.domain.ticket.entity.TicketInfo;
import com.example.choiceculture.domain.festival.enums.ReFundState;
import com.example.choiceculture.domain.ticket.repository.TicketInfoRepository;
import com.example.choiceculture.domain.member.entity.Member;
import com.example.choiceculture.domain.member.repository.MemberRepository;
import com.example.choiceculture.dto.PageRequestDTO;
import com.example.choiceculture.dto.PageResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class TicketInfoServiceImpl implements TicketInfoService {
    private final TicketInfoRepository ticketInfoRepository;
    private final MemberRepository memberRepository;


    @Override
    public List<String> seatList(TicketSeatDTO seatDTO) {
        List<String> infoList = ticketInfoRepository.
                findByFestivalIdAndDateId(seatDTO.getFestivalId(), seatDTO.getDateId());
        if (infoList.isEmpty()) {
            throw new EntityNotFoundException("선택된 좌석이 없습니다.");
        }
        return infoList;
    }

    @Override
    public void add(TicketInfoDTO infoDTO) {
        Member member = memberRepository.findById(infoDTO.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        log.info("🟢 add() 실행: orderId={}, memberId={}, festivalId={}",
                infoDTO.getOrderId(), infoDTO.getMemberId(), infoDTO.getFestivalId());

        infoDTO.getLocationNum().forEach(s -> {
            log.info("🔵 좌석 검사: locationNum={}, orderId={}", s, infoDTO.getOrderId());

            boolean exists = ticketInfoRepository.existsByLocationNumAndOrderId(s, infoDTO.getOrderId());
            log.info("🟠 존재 여부: {}", exists);

            if (!exists) {
                String modifiedOrderId = infoDTO.getOrderId() + "-" + s; // ✅ orderId 변형 확인
                TicketInfo ticketInfo = TicketInfo.builder()
                        .orderId(modifiedOrderId)
                        .festivalId(infoDTO.getFestivalId())
                        .member(member)
                        .dateId(infoDTO.getDateId())
                        .paymentDate(LocalDate.now())
                        .locationNum(s)
                        .build();

                log.info("🟢 저장 시도: orderId={}, locationNum={}", modifiedOrderId, s);

                try {
                    ticketInfoRepository.saveAndFlush(ticketInfo);
                    log.info("✅ 저장 성공: {}", modifiedOrderId);
                } catch (Exception e) {
                    log.error("❌ 저장 실패: {}", modifiedOrderId, e);
                }
            } else {
                log.warn("🚨 이미 존재하는 티켓: {}", s);
            }
        });
    }


    @Override
    public void delete(String ticketId) {
        ticketInfoRepository.deleteById(ticketId);
    }

    @Override
    public PageResponseDTO<TicketResponseDTO> myTickets(PageRequestDTO requestDTO) {
        Page<TicketResponseDTO> pageResult = ticketInfoRepository.getTickets(requestDTO);
        pageResult.forEach(ticket -> ticket.setRefundStateName(ticket.getRefundState().getDescription()));

        return PageResponseDTO.<TicketResponseDTO>withAll()
                .dtoList(pageResult.getContent())
                .totalCount(pageResult.getTotalElements())
                .pageRequestDTO(requestDTO)
                .build();
    }

    @Override
    public void myRefund(String userId, String orderId, String locationNum) {
        String[] seats = locationNum.split(", ");
        for (String seat : seats) {
            String orderIdPrefix = orderId + '-' + seat;
            ticketInfoRepository.findByOrderIdAndUserId(orderIdPrefix, userId).ifPresent(ticket -> {
                ticket.setRefundState(ReFundState.REQUEST);
            });
        }
    }
}
