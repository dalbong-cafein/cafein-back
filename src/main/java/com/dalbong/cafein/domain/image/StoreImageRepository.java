package com.dalbong.cafein.domain.image;

import com.dalbong.cafein.domain.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreImageRepository extends JpaRepository<StoreImage,Long> {

    List<StoreImage> findByStore(Store store);
}
