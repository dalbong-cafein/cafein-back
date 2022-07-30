package com.dalbong.cafein.web.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RecommendRepository extends JpaRepository<Recommend, Long>, RecommendRepositoryQuerydsl {

    Optional<Recommend> findByStoreStoreIdAndSessionId(Long storeId, String sessionId);

}
