package com.dalbong.cafein.domain.store;

import com.dalbong.cafein.domain.congestion.QCongestion;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.dalbong.cafein.domain.congestion.QCongestion.congestion;
import static com.dalbong.cafein.domain.heart.QHeart.heart;
import static com.dalbong.cafein.domain.image.QMemberImage.memberImage;
import static com.dalbong.cafein.domain.memo.QStoreMemo.storeMemo;
import static com.dalbong.cafein.domain.store.QStore.store;
import static org.aspectj.util.LangUtil.isEmpty;

public class StoreRepositoryImpl implements StoreRepositoryQuerydsl{

    private final JPAQueryFactory queryFactory;

    public StoreRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     * 앱단 가게 리스트 조회
     */
    @Override
    public List<Object[]> getStoreList(String keyword) {

        QCongestion subCongestion = new QCongestion("sub");

       List<Tuple> result = queryFactory
                .select(store , store.heartList.size(), JPAExpressions
                        .select(subCongestion.congestionScore.avg())
                        .from(subCongestion)
                        .where(subCongestion.regDateTime.between(LocalDateTime.now().minusHours(1), LocalDateTime.now()),
                                subCongestion.store.storeId.eq(store.storeId)))
                .from(store)
                .leftJoin(store.businessHours).fetchJoin()
                .where(containStoreNameOrAddress(keyword))
                .groupBy(store.storeId)
                .fetch();

        return result.stream().map(t -> t.toArray()).collect(Collectors.toList());
    }

    /**
     * 앱단 내 카페 리스트 조회
     */
    @Override
    public List<Object[]> getMyStoreList(Long principalId) {

        QCongestion subCongestion = new QCongestion("sub");
        List<Tuple> result = queryFactory
                .select(store, JPAExpressions
                        .select(subCongestion.congestionScore.avg())
                        .from(subCongestion)
                        .where(subCongestion.regDateTime.between(LocalDateTime.now().minusHours(1), LocalDateTime.now()),
                                subCongestion.store.storeId.eq(store.storeId))
                        .groupBy(subCongestion.store.storeId))
                .from(store)
                .leftJoin(store.businessHours).fetchJoin()
                .join(heart).on(heart.store.storeId.eq(store.storeId))
                .where(heart.member.memberId.eq(principalId))
                .orderBy(heart.heartId.desc())
                .fetch();

        return result.stream().map(t -> t.toArray()).collect(Collectors.toList());
    }

    /**
     * 엡단 내 카페 리스트 개수 지정 조회
     */
    @Override
    public List<Object[]> getCustomLimitMyStoreList(int limit, Long principalId) {
        QCongestion subCongestion = new QCongestion("sub");

        List<Tuple> result = queryFactory
                .select(store, JPAExpressions
                        .select(subCongestion.congestionScore.avg())
                        .from(subCongestion)
                        .where(subCongestion.regDateTime.between(LocalDateTime.now().minusHours(1), LocalDateTime.now()),
                                subCongestion.store.storeId.eq(store.storeId))
                        .groupBy(subCongestion.store.storeId))
                .from(store)
                .leftJoin(store.businessHours).fetchJoin()
                .join(heart).on(heart.store.storeId.eq(store.storeId))
                .where(heart.member.memberId.eq(principalId))
                .orderBy(heart.heartId.desc())
                .limit(limit)
                .fetch();

        return result.stream().map(t -> t.toArray()).collect(Collectors.toList());
    }

    /**
     * 앱단 본인이 등록한 가게 리스트 조회
     */
    @Override
    public List<Object[]> getMyRegisterStoreList(Long principalId) {

        QCongestion subCongestion = new QCongestion("sub");

        List<Tuple> result = queryFactory.select(store, JPAExpressions
                .select(subCongestion.congestionScore.avg())
                .from(subCongestion)
                .where(subCongestion.regDateTime.between(LocalDateTime.now().minusHours(1), LocalDateTime.now()),
                        subCongestion.store.storeId.eq(store.storeId)))
                .from(store)
                .where(store.regMember.memberId.eq(principalId))
                .fetch();

        return result.stream().map(t -> t.toArray()).collect(Collectors.toList());
    }

    /**
     * 앱단 카페 상세 페이지 조회
     */
    @Override
    public Optional<Object[]> getDetailStore(Long storeId) {

        Tuple tuple = queryFactory.select(store, memberImage)
                .from(store)
                .leftJoin(store.businessHours).fetchJoin()
                .leftJoin(store.modMember).fetchJoin()
                .leftJoin(memberImage).on(memberImage.member.memberId.eq(store.modMember.memberId))
                .where(store.storeId.eq(storeId))
                .fetchOne();

        return tuple != null ? Optional.ofNullable(tuple.toArray()) : Optional.empty();
    }

    /**
     * 추천 검색 카페 리스트 조회
     */
    @Override
    public List<Store> getRecommendSearchStoreList(String keyword) {

        return queryFactory.selectFrom(store)
                .where(containStoreNameOrAddress(keyword))
                .limit(10)
                .fetch();
    }

    /**
     * 관리자단 카페 리스트 조회
     */
    @Override
    public Page<Object[]> getAllStoreList(String[] searchType, String keyword, Pageable pageable) {

        QCongestion subCongestion = new QCongestion("sub");

        JPAQuery<Tuple> query = queryFactory
                .select(store, store.reviewList.size(), JPAExpressions
                        .select(subCongestion.congestionScore.avg())
                        .from(subCongestion)
                        .where(subCongestion.regDateTime.between(LocalDateTime.now().minusHours(1), LocalDateTime.now()),
                                subCongestion.store.storeId.eq(store.storeId)),
                        storeMemo.memoId)
                .from(store)
                .leftJoin(storeMemo).on(storeMemo.store.storeId.eq(store.storeId))
                .where(searchKeyword(searchType, keyword))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .groupBy(store.storeId);

        //정렬
        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(Store.class, "store");
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        List<Tuple> results = query.fetch();

        //count 쿼리
        JPAQuery<Tuple> countQuery = queryFactory
                .select(store, store.reviewList.size(), JPAExpressions
                        .select(subCongestion.congestionScore.avg())
                        .from(subCongestion)
                        .where(subCongestion.regDateTime.between(LocalDateTime.now().minusHours(1), LocalDateTime.now()),
                                subCongestion.store.storeId.eq(store.storeId)))
                .from(store)
                .where(searchKeyword(searchType, keyword))
                .groupBy(store.storeId);

        List<Object[]> content = results.stream().map(t -> t.toArray()).collect(Collectors.toList());

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchCount());

    }

    /**
     * 관리자단 카페 상세 페이지 조회
     */
    @Override
    public Optional<Object[]> getDetailStoreOfAdmin(Long storeId) {

        Tuple tuple = queryFactory.select(store, store.heartList.size(), JPAExpressions
                        .select(congestion.count()).from(congestion).where(congestion.store.storeId.eq(store.storeId))
                , store.reviewList.size())
                .from(store)
                .leftJoin(store.businessHours).fetchJoin()
                .where(store.storeId.eq(storeId))
                .fetchOne();

        return tuple != null ? Optional.ofNullable(tuple.toArray()) : Optional.empty();
    }



    private BooleanBuilder searchKeyword(String[] searchType, String keyword) {

        BooleanBuilder builder = new BooleanBuilder();

        if (searchType != null){
            for(String t : searchType){
                switch (t){
                    case "s":
                        builder.or(containStoreId(keyword));
                        break;
                    case "sn":
                        builder.or(containStoreName(keyword));
                        break;
                    case "a":
                        builder.or(containAddress(keyword));
                        break;
                }
            }
        }
        return builder;
    }

    private BooleanBuilder containStoreNameOrAddress(String keyword){
        BooleanBuilder builder = new BooleanBuilder();

        BooleanBuilder storeNameBuilder = new BooleanBuilder();
        storeNameBuilder.and(containStoreName(keyword));
        storeNameBuilder.and(sggNmEq(keyword));

        builder.or(storeNameBuilder);
        builder.or(containAddress(keyword));
        return builder;
    }

    private BooleanExpression sggNmEq(String keyword) {

        if(!isEmpty(keyword)){
            //키워드에 구 데이터가 있는 체크
            for(String sgg : sggList){
                if(keyword.contains(sgg)){
                    //구에 해당하는 카페 조건 추가
                    return store.address.sggNm.contains(sgg);
                }
            }
        }

        return null;
    }

    private BooleanExpression containStoreId(String keyword) {

        Long storeId = null;

        try {
            storeId = Long.parseLong(keyword);
        }catch (NumberFormatException e){
            e.getMessage();
        }

        return !isEmpty(keyword) && storeId != null? store.storeId.eq(storeId) : null;

    }

    private BooleanExpression containStoreName(String keyword) {


        if(!isEmpty(keyword)){
            String replaceWord = keyword;

            if(keyword.contains("투썸 플레이스")){
                replaceWord = keyword.replace("투썸 플레이스", "투썸");
            }else if (keyword.contains("스벅")){
                replaceWord = keyword.replace("스벅", "스타벅스");
            }
            System.out.println("------");
            System.out.println(replaceWord);
            //키워드에 구 데이터가 있는 체크
            for(String sgg : sggList){
                if(replaceWord.contains(sgg)){
                    //구 이름이 있으면 띄어쓰기 전까지 문자 삭제
                    int startIdx = replaceWord.indexOf(sgg);
                    int endIdx = replaceWord.indexOf(" ", startIdx);

                    String deleteWord;
                    if(endIdx < 0){
                        deleteWord = replaceWord.substring(startIdx);
                    }else {
                        deleteWord = replaceWord.substring(startIdx, endIdx);
                    }
                    System.out.println("startIdx: " + startIdx + " endIdx: " + endIdx);

                    String result = replaceWord.replace(deleteWord, "").strip();

                    return !isEmpty(result) ? store.storeName.contains(result) : store.storeName.contains(replaceWord.replace(sgg, ""));
                }
            }
            return store.storeName.contains(replaceWord);
        }

        return null;

    }

    private BooleanExpression containAddress(String keyword) {

        return !isEmpty(keyword) ? store.address.fullAddress.contains(keyword) : null;

    }

    private final String[] sggList = {"서대문","마포","노원","동대문","종로","강남"};
}
