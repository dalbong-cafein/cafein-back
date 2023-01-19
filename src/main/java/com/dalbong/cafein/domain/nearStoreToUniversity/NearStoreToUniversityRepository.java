package com.dalbong.cafein.domain.nearStoreToUniversity;

import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.university.University;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NearStoreToUniversityRepository extends JpaRepository<NearStoreToUniversity,Long> {

    boolean existsByStoreAndUniversity(Store store, University university);
}
