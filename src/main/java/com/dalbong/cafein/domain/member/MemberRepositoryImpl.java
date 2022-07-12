package com.dalbong.cafein.domain.member;

import com.dalbong.cafein.domain.congestion.QCongestion;
import com.dalbong.cafein.domain.heart.QHeart;
import com.dalbong.cafein.domain.image.QMemberImage;
import com.dalbong.cafein.domain.memo.QMemberMemo;
import com.dalbong.cafein.domain.report.QReport;
import com.dalbong.cafein.domain.review.QReview;
import com.dalbong.cafein.domain.sticker.QSticker;
import com.dalbong.cafein.dto.admin.member.AdminDetailMemberResDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.dalbong.cafein.domain.congestion.QCongestion.congestion;
import static com.dalbong.cafein.domain.heart.QHeart.heart;
import static com.dalbong.cafein.domain.image.QMemberImage.memberImage;
import static com.dalbong.cafein.domain.member.QMember.member;
import static com.dalbong.cafein.domain.memo.QMemberMemo.memberMemo;
import static com.dalbong.cafein.domain.report.QReport.report;
import static com.dalbong.cafein.domain.review.QReview.review;
import static com.dalbong.cafein.domain.sticker.QSticker.sticker;
import static com.dalbong.cafein.domain.store.QStore.store;
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
    public Optional<Member> findByKakaoIdAndNotLeave(String kakaoId) {

        Member findMember = queryFactory.selectFrom(member)
                .join(member.roleSet)
                .where(member.kakaoId.eq(kakaoId), member.state.ne(MemberState.LEAVE))
                .fetchOne();
        return findMember != null ? Optional.of(findMember) : Optional.empty();
    }

    /**
     * 네이버 계정 기존 회원 찾기
     */
    @Override
    public Optional<Member> findByNaverIdAndNotLeave(String naverId) {

        Member findMember = queryFactory.selectFrom(member)
                .join(member.roleSet)
                .where(member.naverId.eq(naverId), member.state.ne(MemberState.LEAVE))
                .fetchOne();
        return findMember != null ? Optional.of(findMember) : Optional.empty();
    }

    /**
     * 닉네임 중복확인
     */
    @Override
    public boolean existNickname(String nickname) {

        Integer fetchOne = queryFactory.selectOne()
                .from(member)
                .where(member.nickname.eq(nickname),
                        member.state.ne(MemberState.LEAVE))
                .fetchOne();

        return fetchOne != null;
    }

    /**
     * 관리자단 전체 회원 리스트 조회
     */
    @Override
    public Page<Object[]> getAllMemberListOfAdmin(String[] searchType, String keyword, Pageable pageable) {


        JPAQuery<Tuple> query = queryFactory.select(member, memberImage, memberMemo.memoId)
                .from(member)
                .leftJoin(memberImage).on(memberImage.member.memberId.eq(member.memberId))
                .leftJoin(memberMemo).on(memberMemo.member.memberId.eq(member.memberId))
                .where(searchKeyword(searchType, keyword))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());


        //정렬
        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(Member.class, "member1");
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }


        List<Tuple> results = query.fetch();

        //count 쿼리
        JPAQuery<Tuple> countQuery = queryFactory.select(member, memberImage)
                .from(member)
                .leftJoin(memberImage).on(memberImage.member.eq(member))
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

    /**
     * 관리자단 회원 상세 조회
     */
    @Override
    public Optional<Object[]> getDetailMemberOfAdmin(Long memberId) {



        Tuple tuple = queryFactory.select(member, memberImage,
                JPAExpressions.select(heart.count())
                        .from(heart)
                        .where(heart.member.memberId.eq(memberId)),
                JPAExpressions.select(congestion.count())
                        .from(congestion)
                        .where(congestion.member.memberId.eq(memberId)),
                JPAExpressions.select(review.count())
                        .from(review)
                        .where(review.member.memberId.eq(memberId)),
                JPAExpressions.select(sticker.count())
                        .from(sticker)
                        .where(sticker.member.memberId.eq(memberId))
        )
                .from(member)
                .leftJoin(memberImage).on(memberImage.member.memberId.eq(member.memberId))
                .where(member.memberId.eq(memberId))
                .fetchOne();

        return tuple != null ? Optional.of(tuple.toArray()) : Optional.empty();
    }

    /**
     * 관리자단 오늘 등록된 회원 수 조회
     */
    @Override
    public Long getRegisterCountOfToday() {
        return queryFactory
                .select(member.count())
                .from(member)
                .where(member.regDateTime.between(LocalDateTime.now().toLocalDate().atStartOfDay(),
                        LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59))))
                .fetchOne();
    }

    /**
     * 회원 정지기간 만료 회원 리스트 조회
     */
    @Override
    public List<Member> findByReportExpiredToday() {

        return queryFactory.select(member)
                .from(member)
                .where(member.reportExpiredDateTime.between(LocalDateTime.now().toLocalDate().atStartOfDay(),
                        LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59))))
                .fetch();
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
