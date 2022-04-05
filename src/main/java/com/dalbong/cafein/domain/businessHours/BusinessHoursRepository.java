package com.dalbong.cafein.domain.businessHours;

import com.dalbong.cafein.domain.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusinessHoursRepository extends JpaRepository<BusinessHours, Long> {

    Optional<BusinessHours> findByStore(Store store);

}
