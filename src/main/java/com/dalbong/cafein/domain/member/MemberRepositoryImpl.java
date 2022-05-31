package com.dalbong.cafein.domain.member;

import com.dalbong.cafein.domain.review.Review;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.dalbong.cafein.domain.coupon.QCoupon.coupon;
import static com.dalbong.cafein.domain.image.QMemberImage.memberImage;
import static com.dalbong.cafein.domain.member.QMember.member;
import static com.dalbong.cafein.domain.review.QReview.review;
import static org.aspectj.util.LangUtil.isEmpty;

public class MemberRepositoryImpl implements MemberRepositoryQuerydsl{

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     * 카카오 계정 기존 회원 찾기
     */
    @Override
    public Optional<Member> findByKakaoIdAndNotDeleted(String kakaoId) {

        Member findMember = queryFactory.selectFrom(member)
                .join(member.roleSet)
                .where(member.kakaoId.eq(kakaoId), member.isDeleted.isFalse().or(QMember.member.isDeleted.isNull()))
                .fetchOne();
        return findMember != null ? Optional.of(findMember) : Optional.empty();
    }

    /**
     * 네이버 계정 기존 회원 찾기
     */
    @Override
    public Optional<Member> findByNaverIdAndNotDeleted(String naverId) {

        Member findMember = queryFactory.selectFrom(member)
                .join(member.roleSet)
                .where(member.naverId.eq(naverId), member.isDeleted.isFalse().or(QMember.member.isDeleted.isNull()))
                .fetchOne();
        return findMember != null ? Optional.of(findMember) : Optional.empty();
    }

    /**
     * 관리자단 전체 회원 리스트 조회
     */
    @Override
    public Page<Object[]> getAllMemberListOfAdmin(String[] searchType, String keyword, Pageable pageable) {

        JPAQuery<Tuple> query = queryFactory.select(member, member.memberId)
                .from(member)
                .where(searchKeyword(searchType, keyword))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        //정렬
        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(Member.class, "member");
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        List<Tuple> results = query.fetch();

        //count 쿼리
        JPAQuery<Tuple> countQuery = queryFactory.select()
                .from(member)
                .where(searchKeyword(searchType, keyword));

        List<Object[]> content = results.stream().map(t -> t.toArray()).collect(Collectors.toList());

        /**
         * 두 경우에는 count 쿼리를 날리지 않음 -> spring Jpa가 지원 (PageableExeutionUtils)
         * 1. 시작 페이지이면서 content 수가 page size보다 작을 경우.
         * 2. 마지막 페이지이면서 content 수가 page size보다 작을 경우.
         * 날릴 필요가 없음.
         */
        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchCount());
    }

    private BooleanBuilder searchKeyword(String[] searchType, String keyword) {

        BooleanBuilder builder = new BooleanBuilder();

        if (searchType != null){ ;
            for(String t : searchType){
                switch (t){
                    case "m":
                        builder.or(containMemberId(keyword));
                        break;
                    case "mn":
                        builder.or(containNickname(keyword));
                        break;
                    case "p":
                        builder.or(containPhone(keyword));
                        break;
                }
            }
        }
        return builder;
    }

    private BooleanExpression containNickname(String keyword) {
        return !isEmpty(keyword) ? member.nickname.eq(keyword) : null;
    }

    private BooleanExpression containPhone(String keyword) {
        return !isEmpty(keyword) ? member.phone.eq(keyword) : null;
    }

    private BooleanExpression containMemberId(String keyword) {

        Long memberId = null;

        try {
            memberId = Long.parseLong(keyword);
        }catch (NumberFormatException e){
            e.getMessage();
        }

        return !isEmpty(keyword) && memberId != null ? member.memberId.eq(memberId) : null;

    }
}
