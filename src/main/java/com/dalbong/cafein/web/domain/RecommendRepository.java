package com.dalbong.cafein.web.domain;

import com.dalbong.cafein.domain.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RecommendRepository extends JpaRepository<Recommend, Long>, RecommendRepositoryQuerydsl {

    Optional<Recommend> findByStoreStoreIdAndSessionId(Long storeId, String sessionId);

    List<Recommend> findByStore(Store store);

}
