package com.example.choiceculture.domain.ticket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TicketSeatDTO {
    private Integer festivalId;
    private Integer dateId;
}
