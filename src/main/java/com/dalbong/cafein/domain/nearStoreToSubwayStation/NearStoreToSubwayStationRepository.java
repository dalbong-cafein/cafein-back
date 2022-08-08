package com.dalbong.cafein.domain.nearStoreToSubwayStation;

import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.subwayStation.SubwayStation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NearStoreToSubwayStationRepository extends JpaRepository<NearStoreToSubwayStation, Long> {

    boolean existsByStoreAndSubwayStation(Store store, SubwayStation subwayStation);

}
