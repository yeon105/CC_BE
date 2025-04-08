package com.example.choiceculture.domain.ticket.dto;

import com.example.choiceculture.domain.festival.enums.ReFundState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TicketResponseDTO {
    private String orderId;
    private String userName;
    private String festivalName;
    private String date;
    private String locationNum;
    private LocalDate paymentDate;
    private ReFundState refundState;
    private String refundStateName;
    private Integer totalPrice;
    private String postImage;

}
