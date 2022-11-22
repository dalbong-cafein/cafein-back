package com.dalbong.cafein.domain.image;

import com.dalbong.cafein.domain.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StoreImageRepository extends JpaRepository<StoreImage,Long> {

    List<StoreImage> findByStore(Store store);

    @Query("select si from StoreImage si " +
            "join fetch si.regMember " +
            "where si.store.storeId =:storeId")
    List<StoreImage> findWithRegMemberByStoreId(Long storeId);
}
