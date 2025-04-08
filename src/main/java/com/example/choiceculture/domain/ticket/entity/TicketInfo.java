package com.example.choiceculture.domain.ticket.entity;

import com.example.choiceculture.domain.festival.enums.ReFundState;
import com.example.choiceculture.domain.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "ticket_info")
public class TicketInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ✅ 자동 증가 ID 추가
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Size(max = 50)
    @Column(name = "order_id", nullable = false, length = 50)
    private String orderId;

    @NotNull
    @Column(name = "festival_id", nullable = false)
    private Integer festivalId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "date_id")
    private Integer dateId;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "refund_state")
    private ReFundState refundState;

    @Size(max = 5)
    @Column(name = "location_num", length = 5)
    private String locationNum;

    @PreUpdate
    @PrePersist
    public void prePersist() {
        if (this.refundState == null) {
            this.refundState = ReFundState.YET;
        }
    }

}