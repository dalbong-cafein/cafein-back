package com.dalbong.cafein.domain.store;

import com.dalbong.cafein.domain.store.dto.NearStoreDto;
import com.querydsl.jpa.JPAExpressions;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryQuerydsl {

    Optional<Store> findByStoreName(String storeName);

    @Query("select s from Store s where s.latY is null AND s.lngX is null")
    List<Store> findByLatYAndLngXIsNull();

    List<Store> findByAddress_SggNm(String sggNm);


    @Query(value = "select * , (6371*acos(cos(radians(:latY))*cos(radians(s.latY))*cos(radians(s.lngX) " +
            "-radians(:lngX))+sin(radians(:latY))*sin(radians(s.latY)))) AS distance " +
            "from store s " +
            "where s.store_id <> :storeId " +
            "having distance < 0.5 " +
            "order by distance " +
            "limit 10", nativeQuery = true)
    List<Store> recommendNearStore(@Param("storeId") Long storeId, @Param("latY") double latY, @Param("lngX") double lngX);

    @Query("select count(s) from Store s where s.regMember.memberId =:memberId")
    int countByRegMemberId(@Param("memberId") Long regMemberId);

    @Query("select s from Store s where s.phone is null or s.phone = ''")
    List<Store> findByPhoneIsNullOrBlank();

}
