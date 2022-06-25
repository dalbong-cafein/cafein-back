package com.dalbong.cafein.domain.store;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryQuerydsl {

    Optional<Store> findByStoreName(String storeName);

    @Query("select s from Store s where s.latY is null AND s.lngX is null")
    List<Store> findByLatYAndLngXIsNull();

}
