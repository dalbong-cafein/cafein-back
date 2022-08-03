package com.dalbong.cafein.web.domain;

import com.dalbong.cafein.domain.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecommendRepository extends JpaRepository<Recommend, Long>, RecommendRepositoryQuerydsl {

    Optional<Recommend> findByStoreStoreIdAndSessionId(Long storeId, String sessionId);

    @Query("select rec from Recommend rec where rec.store.storeId =:storeId")
    List<Recommend> findByStoreId(@Param("storeId") Long storeId);

}
