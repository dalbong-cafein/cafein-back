package com.dalbong.cafein.domain.congestion;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CongestionRepository extends JpaRepository<Congestion, Long>, CongestionQuerydsl {

}
