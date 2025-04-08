package com.example.choiceculture.domain.common.repository;

import com.example.choiceculture.domain.common.entity.CommonInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommonInfoRepository extends JpaRepository<CommonInfo, String> {

    @Query(value = "select c from CommonInfo c where c.id like CONCAT(:id, '%') and c.useYn='Y'")
    List<CommonInfo> list(@Param("id") String id);

    @Query(value = "select count(c) from CommonInfo c where c.id like CONCAT(:id, '%')")
    Integer countById(@Param("id") String id);

    @Query(value = "select c from CommonInfo c where c.id like CONCAT(:id, '%') and c.useYn='Y' and c.id != 'CT01' ")
    List<CommonInfo> listCategory(@Param("id") String id);

}
