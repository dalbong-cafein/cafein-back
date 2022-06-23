package com.dalbong.cafein.domain.congestion;

import com.dalbong.cafein.domain.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CongestionRepository extends JpaRepository<Congestion, Long>, CongestionRepositoryQuerydsl {

    @Query("select c from Congestion c left join fetch c.store where c.congestionId =:congestionId")
    Optional<Congestion> findByIdStoreFetch(@Param("congestionId") Long congestionId);

    List<Congestion> findByStore(Store store);

}
