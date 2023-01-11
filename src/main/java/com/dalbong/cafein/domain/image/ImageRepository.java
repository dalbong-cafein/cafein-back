package com.dalbong.cafein.domain.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image,Long> {

    @Query(value = "select i from StoreImage si " +
            "join Image i on i.imageId = si.imageId " +
            "where si.store.storeId =:storeId and i.isRepresent = true " +
            "UNION " +
            "select i from ReviewImage ri " +
            "join Image i on i.imageId = ri.imageId " +
            "join Review r on r.reviewId = ri.reviewId " +
            "where r.store.storeId =:storeId and i.isRepresent = true", nativeQuery = true)
    Optional<Image> getRepresentativeImage(@Param("storeId") Long storeId);

}
