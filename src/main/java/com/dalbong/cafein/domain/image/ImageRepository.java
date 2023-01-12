package com.dalbong.cafein.domain.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image,Long> {

    @Query(value = "select i.image_id from store_image si " +
            "join image i on i.image_id = si.image_id " +
            "where si.store_id =:storeId and i.is_represent = true " +
            "UNION " +
            "select i.image_id from review_image ri " +
            "join image i on i.image_id = ri.image_id " +
            "join review r on r.review_id = ri.review_id " +
            "where r.store_id =:storeId and i.is_represent = true", nativeQuery = true)
   Long getRepresentativeImageOfStore(@Param("storeId") Long storeId);

}
