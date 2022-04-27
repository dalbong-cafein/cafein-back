package com.dalbong.cafein.domain.congestion;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;

import static com.dalbong.cafein.domain.congestion.QCongestion.congestion;

public class CongestionRepositoryImpl implements CongestionQuerydsl{

    private final JPAQueryFactory queryFactory;

    public CongestionRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     * 혼잡도 3시간이내 등록 여부
     */
    @Override
    public boolean existWithinTime(Long storeId, Long principalId) {

        Integer fetchOne = queryFactory.selectOne()
                .from(congestion)
                .where(congestion.store.storeId.eq(storeId),
                        congestion.member.memberId.eq(principalId),
                        congestion.regDateTime.between(LocalDateTime.now().minusHours(3),LocalDateTime.now()))
                .fetchFirst();//limit 1

        return fetchOne != null;
    }
}
