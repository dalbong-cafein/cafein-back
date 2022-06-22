package com.dalbong.cafein.domain.memo;

import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreMemoRepository extends JpaRepository<StoreMemo,Long> {

    void deleteByStore(Store store);
}
