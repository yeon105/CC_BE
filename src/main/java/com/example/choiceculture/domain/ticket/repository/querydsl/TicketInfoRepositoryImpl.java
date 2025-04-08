package com.example.choiceculture.domain.ticket.repository.querydsl;

import com.example.choiceculture.domain.ticket.dto.TicketResponseDTO;
import com.example.choiceculture.domain.festival.enums.ReFundState;
import com.example.choiceculture.dto.PageRequestDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Objects;

import static com.example.choiceculture.domain.festival.entity.QFestivalInfo.festivalInfo;
import static com.example.choiceculture.domain.festival.entity.QFestivalTime.festivalTime;
import static com.example.choiceculture.domain.ticket.entity.QTicketInfo.ticketInfo;

@Slf4j
@RequiredArgsConstructor
public class TicketInfoRepositoryImpl implements TicketInfoRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    StringExpression orderIdPrefix = ticketInfo.orderId.substring(0, ticketInfo.orderId.length().subtract(4));

    @Override
    public Page<TicketResponseDTO> getTickets(PageRequestDTO requestDTO) {
        Pageable pageable = PageRequest.of(
                requestDTO.getPage() - 1,
                requestDTO.getSize(),
                Sort.by("paymentDate").ascending()
        );

        List<TicketResponseDTO> dtoList = jpaQueryFactory
                .select(Projections.fields(TicketResponseDTO.class,
                        orderIdPrefix.as("orderId"),
                        ticketInfo.member.userName,
                        festivalInfo.postImage,
                        festivalInfo.festivalName,
                        sumDate().as("date"),
                        sumLocationNum().as("locationNum"),
                        ticketInfo.paymentDate,
                        ticketInfo.refundState,
                        sumTicketPrice().as("totalPrice")
                ))
                .from(ticketInfo)
                .where(containsUserName(requestDTO.getSearchTerm()),
                        containsFestivalName(requestDTO.getSearchFestivalName()),
                        eqRefundState(requestDTO.getRefundState()),
                        eqUserId(requestDTO.getUserId()))
                .leftJoin(festivalInfo).on(ticketInfo.festivalId.eq(festivalInfo.id))
                .leftJoin(festivalTime).on(ticketInfo.dateId.eq(festivalTime.id)).fetchJoin()
                .groupBy(orderIdPrefix,
                        ticketInfo.member.userName,
                        festivalInfo.festivalName,
                        festivalInfo.postImage,
                        festivalTime.date,
                        festivalTime.time,
                        festivalInfo.salePrice,
                        ticketInfo.paymentDate,
                        ticketInfo.refundState)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = jpaQueryFactory
                .select(orderIdPrefix.countDistinct())
                .from(ticketInfo)
                .where(containsUserName(requestDTO.getSearchTerm()),
                        containsFestivalName(requestDTO.getSearchFestivalName()),
                        eqRefundState(requestDTO.getRefundState()),
                        eqUserId(requestDTO.getUserId()))
                .leftJoin(festivalInfo).on(ticketInfo.festivalId.eq(festivalInfo.id))
                .leftJoin(festivalTime).on(ticketInfo.festivalId.eq(festivalTime.id))
                .groupBy(orderIdPrefix,
                        ticketInfo.member.userName,
                        festivalInfo.festivalName,
                        festivalInfo.postImage,
                        festivalTime.date,
                        festivalTime.time,
                        festivalInfo.salePrice,
                        ticketInfo.paymentDate,
                        ticketInfo.refundState)
                .fetch().size();

        return new PageImpl<>(dtoList, pageable, totalCount);

    }

    private BooleanExpression eqUserId(String userId) {
        if (userId == null) {
            return null;
        }
        return ticketInfo.member.id.eq(userId);
    }

    private StringExpression sumDate() {
        return Expressions.stringTemplate(
                "CONCAT({0}, ' ', {1})",
                festivalTime.date, festivalTime.time
        );
    }

    private StringExpression sumLocationNum() {
        return Expressions.stringTemplate(
                "REPLACE(CONCAT('', GROUP_CONCAT(DISTINCT {0})), ',', ', ')",
                ticketInfo.locationNum
        );
    }

    private NumberExpression<Integer> sumTicketPrice() {
        return Expressions.numberTemplate(Integer.class,
                "{0} * COUNT({1})",
                festivalInfo.salePrice, orderIdPrefix
        );
    }

    private BooleanExpression containsUserName(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }
        return ticketInfo.member.userName.like("%" + keyword + "%");
    }

    private BooleanExpression containsFestivalName(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }
        return festivalInfo.festivalName.like("%" + keyword + "%");
    }

    private BooleanExpression eqRefundState(String refundState) {
        if (refundState == null || refundState.isBlank() || Objects.equals(refundState, "ALL")) {
            return null;
        }
        return ticketInfo.refundState.eq(ReFundState.valueOf(refundState));
    }

}
