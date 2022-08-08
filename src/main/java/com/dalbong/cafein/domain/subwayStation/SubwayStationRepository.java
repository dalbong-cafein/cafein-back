package com.dalbong.cafein.domain.subwayStation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SubwayStationRepository extends JpaRepository<SubwayStation,Long> {

    boolean existsByStationId(int stationId);

    List<SubwayStation> findByIsUseTrue();

}
