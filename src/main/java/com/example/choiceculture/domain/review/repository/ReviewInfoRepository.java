package com.example.choiceculture.domain.review.repository;

import com.example.choiceculture.domain.review.entity.ReviewInfo;
import com.example.choiceculture.domain.festival.enums.ReviewType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewInfoRepository extends JpaRepository<ReviewInfo, Integer> {
    List<ReviewInfo> findByType(ReviewType type);

    @Query(value = "select r from ReviewInfo r where r.memberId=:userId and r.type=:type")
    List<ReviewInfo> findTypeByMemberId(@Param("userId") String userId, @Param("type") ReviewType type);

    @Query("select avg(r.rating) from ReviewInfo r where r.festivalId = :festivalId")
    Double findAverageRating(@Param("festivalId") Integer festivalId);

}
