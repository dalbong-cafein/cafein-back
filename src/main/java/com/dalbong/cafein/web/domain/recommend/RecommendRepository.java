package com.dalbong.cafein.web.domain.recommend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecommendRepository extends JpaRepository<Recommend, Long>, RecommendRepositoryQuerydsl {

    Recommend findByStoreStoreIdAndSessionId(Long storeId, String sessionId);

    @Query("select rec from Recommend rec where rec.store.storeId =:storeId")
    List<Recommend> findByStoreId(@Param("storeId") Long storeId);

}
