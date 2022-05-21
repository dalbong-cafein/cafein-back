package com.dalbong.cafein.domain.congestion;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.dalbong.cafein.domain.congestion.QCongestion.congestion;
import static org.aspectj.util.LangUtil.isEmpty;

public class CongestionRepositoryImpl implements CongestionRepositoryQuerydsl {

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

    /**
     * 카페별 혼잡도 리스트 조회
     */
    @Override
    public List<Congestion> getCongestionList(Long storeId, Integer minusDays) {

        return queryFactory.select(congestion)
                .from(congestion)
                .leftJoin(congestion.member).fetchJoin()
                .where(congestion.store.storeId.eq(storeId), dailyLookup(minusDays))
                .orderBy(congestion.congestionId.desc())
                .fetch();
    }

    private BooleanExpression dailyLookup(Integer minusDays) {

        System.out.println(congestion.regDateTime);
        System.out.println(LocalDate.now().minusDays(minusDays).atTime(0,0));
        System.out.println(LocalDate.now().minusDays(minusDays).atTime(23,59,59));

        return minusDays != null ?
                congestion.regDateTime
                        .between(LocalDate.now().minusDays(minusDays).atTime(0,0),
                                LocalDate.now().minusDays(minusDays).atTime(23,59,59)) : null;

    }


}
