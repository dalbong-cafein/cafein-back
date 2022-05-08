package com.dalbong.cafein.domain.review;

import com.dalbong.cafein.domain.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryQuerydsl {

    List<Review> findByStore(Store store);

    @Query("select r from Review r where  r.store.storeId =:storeId")
    List<Review> findByStoreId(@Param("storeId") Long storeId);

}
