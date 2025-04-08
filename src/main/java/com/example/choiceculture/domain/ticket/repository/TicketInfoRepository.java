package com.example.choiceculture.domain.ticket.repository;

import com.example.choiceculture.domain.ticket.entity.TicketInfo;
import com.example.choiceculture.domain.ticket.repository.querydsl.TicketInfoRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TicketInfoRepository extends JpaRepository<TicketInfo, String>, TicketInfoRepositoryCustom {
    @Query(value = "select t.locationNum from TicketInfo t where t.festivalId=:festivalId and t.dateId=:dateId and t.refundState='YET'")
    List<String> findByFestivalIdAndDateId(@Param("festivalId") Integer festivalId, @Param("dateId") Integer dateId);


        boolean existsByLocationNumAndOrderId(String locationNum, String orderId);



    @Query(value = "select t from TicketInfo t where t.orderId=:orderId and t.member.id=:userId")
    Optional<TicketInfo> findByOrderIdAndUserId(@Param("orderId") String orderId, @Param("userId") String userId);
}
