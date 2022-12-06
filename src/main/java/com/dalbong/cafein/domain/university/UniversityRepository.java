package com.dalbong.cafein.domain.university;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UniversityRepository extends JpaRepository<University,Long> {

    boolean existsByMainKey(String mainKey);

    List<University> findByIsUseTrue();
}
