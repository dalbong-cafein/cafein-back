package com.dalbong.cafein.domain.memo;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.dalbong.cafein.domain.memo.QMemo.memo;

public class MemoRepositoryImpl implements MemoRepositoryQuerydsl{

    private final JPAQueryFactory queryFactory;

    public MemoRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     * 관리자단 최근 생성 or 수정된 메모 리스트 개수 지정 조회
     */
    @Override
    public List<Memo> getCustomLimitMemoList(int limit) {

        return queryFactory.select(memo)
                .from(memo)
                .orderBy(memo.modDateTime.desc())
                .limit(limit)
                .fetch();
    }
}
