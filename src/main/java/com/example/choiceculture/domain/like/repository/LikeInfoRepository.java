package com.example.choiceculture.domain.like.repository;

import com.example.choiceculture.domain.like.entity.LikeInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeInfoRepository extends JpaRepository<LikeInfo, Integer> {
    @Query(value = "select l from LikeInfo l where l.memberId=:userId and l.festivalId=:festivalId")
    Optional<LikeInfo> findByDTO(String userId, Integer festivalId);

    @Query(value = "select count(l) from LikeInfo l where l.festivalId=:festivalId")
    Integer findByFestivalId(@Param("festivalId") Integer festivalId);

    @Query("SELECT l FROM LikeInfo l WHERE l.memberId = :userId")
    Page<LikeInfo> findByUserId(@Param("userId") String userId, Pageable pageable);
}
