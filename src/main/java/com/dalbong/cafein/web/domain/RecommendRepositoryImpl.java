package com.dalbong.cafein.web.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.dalbong.cafein.web.domain.QRecommend.recommend;

public class RecommendRepositoryImpl implements RecommendRepositoryQuerydsl{

    private final JPAQueryFactory queryFactory;

    public RecommendRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }


    /**
     * 해당 IP의 일정 시간내(하루) 추천 데이터 등록 여부 조회
     */
    @Override
    public boolean existWithinTime(Long storeId, String ip) {

        Integer fetchOne = queryFactory.selectOne()
                .from(recommend)
                .where(recommend.store.storeId.eq(storeId),
                        recommend.clientIp.eq(ip),
                        recommend.regDateTime.between(LocalDateTime.now().toLocalDate().atStartOfDay(),
                                LocalDateTime.now()))
                .fetchOne();

        return fetchOne != null;
    }
}
