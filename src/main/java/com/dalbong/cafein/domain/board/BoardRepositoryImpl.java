package com.dalbong.cafein.domain.board;

import com.dalbong.cafein.domain.review.Review;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;

import java.util.List;

import static com.dalbong.cafein.domain.board.QBoard.board;
import static com.dalbong.cafein.domain.review.QReview.review;
import static org.aspectj.util.LangUtil.isEmpty;

public class BoardRepositoryImpl implements BoardRepositoryQuerydsl{

    private final JPAQueryFactory queryFactory;

    public BoardRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     * 관리자단 게시글 리스트 조회
     */
    @Override
    public Page<Board> getBoardList(Long boardCategoryId, String keyword, Pageable pageable) {

        JPAQuery<Board> query = queryFactory.select(board)
                .from(board)
                .where(board.boardCategory.boardCategoryId.eq(boardCategoryId),
                        containTitleOrContent(keyword))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        //정렬
        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(Board.class, "board");
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        List<Board> results = query.fetch();

        //count 쿼리
        JPAQuery<Board> countQuery = queryFactory.select(board)
                .from(board)
                .where(board.boardCategory.boardCategoryId.eq(boardCategoryId),
                        containTitleOrContent(keyword))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        return PageableExecutionUtils.getPage(results, pageable, () -> countQuery.fetchCount());
    }

    private BooleanBuilder containTitleOrContent(String keyword) {

        BooleanBuilder builder = new BooleanBuilder();

        builder.or(containTitle(keyword));
        builder.or(containContent(keyword));

        return builder;
    }


    private BooleanExpression containContent(String keyword) {

        return !isEmpty(keyword) ? board.content.contains(keyword) : null;

    }

    private BooleanExpression containTitle(String keyword) {

        return !isEmpty(keyword) ? board.title.contains(keyword) : null;

    }
}
