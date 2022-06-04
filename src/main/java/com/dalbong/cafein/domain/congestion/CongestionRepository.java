package com.dalbong.cafein.domain.congestion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CongestionRepository extends JpaRepository<Congestion, Long>, CongestionRepositoryQuerydsl {

    @Query("select c from Congestion c left join fetch c.store where c.congestionId =:congestionId")
    Optional<Congestion> findByIdStoreFetch(@Param("congestionId") Long congestionId);

}
