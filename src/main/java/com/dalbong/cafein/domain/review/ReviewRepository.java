package com.dalbong.cafein.domain.review;

import com.dalbong.cafein.domain.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryQuerydsl {

    List<Review> findByStore(Store store);

    @Query("select r from Review r where  r.store.storeId =:storeId")
    List<Review> findByStoreId(@Param("storeId") Long storeId);

    long countByStoreStoreId(Long storeId);

    @Query("select r from Review r left join fetch r.store where r.reviewId =:reviewId")
    Optional<Review> findByIdStoreFetch(@Param("reviewId") Long reviewId);

    @Query("select count(r) from Review r where r.member.memberId =:memberId")
    int countByMemberId(@Param("memberId") Long memberId);

}
