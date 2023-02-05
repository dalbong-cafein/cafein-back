package com.dalbong.cafein.domain.store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryQuerydsl {

    Optional<Store> findByStoreName(String storeName);

    @Query("select s from Store s where s.latY is null AND s.lngX is null")
    List<Store> findByLatYAndLngXIsNull();

    List<Store> findByAddress_SggNm(String sggNm);

    @Query("select count(s) from Store s where s.regMember.memberId =:memberId")
    int countByRegMemberId(@Param("memberId") Long regMemberId);

    @Query("select s from Store s where s.phone is null or s.phone = ''")
    List<Store> findByPhoneIsNullOrBlank();

}
