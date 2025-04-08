package com.example.choiceculture.domain.ticket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TicketInfoDTO {
    private String orderId;
    private Integer festivalId;
    private String memberId;
    private Integer dateId;
    private String reFundStateName;
    private List<String> locationNum;

}
