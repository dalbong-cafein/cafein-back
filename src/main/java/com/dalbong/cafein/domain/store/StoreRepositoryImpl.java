package com.dalbong.cafein.domain.store;

import com.dalbong.cafein.domain.address.Address;
import com.dalbong.cafein.domain.congestion.QCongestion;
import com.dalbong.cafein.domain.subwayStation.QSubwayStation;
import com.dalbong.cafein.web.domain.contents.ContentsType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.dalbong.cafein.domain.congestion.QCongestion.congestion;
import static com.dalbong.cafein.domain.heart.QHeart.heart;
import static com.dalbong.cafein.domain.image.QMemberImage.memberImage;
import static com.dalbong.cafein.domain.image.QReviewImage.reviewImage;
import static com.dalbong.cafein.domain.memo.QStoreMemo.storeMemo;
import static com.dalbong.cafein.domain.nearStoreToSubwayStation.QNearStoreToSubwayStation.nearStoreToSubwayStation;
import static com.dalbong.cafein.domain.review.QReview.review;
import static com.dalbong.cafein.domain.store.QStore.store;
import static com.dalbong.cafein.domain.subwayStation.QSubwayStation.subwayStation;
import static com.dalbong.cafein.web.domain.contents.QContentsStore.contentsStore;
import static org.aspectj.util.LangUtil.isEmpty;

public class StoreRepositoryImpl implements StoreRepositoryQuerydsl{

    private final JPAQueryFactory queryFactory;
    private final String[] sggArr = {"서대문","마포","성북","동대문","종로","강남","중","광진","서초"};

    public StoreRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     * 기존 카페 주소 등록 여부 확인
     */
    @Override
    public boolean existAddress(Address address) {

        Integer fetchOne = queryFactory.selectOne()
                .from(store)
                .where(store.address.sggNm.eq(address.getSggNm()),
                        store.address.rNm.eq(address.getRNm()),
                        store.address.rNum.eq(address.getRNum()))
                .fetchOne();

        return fetchOne != null;
    }

    /**
     * 앱단 가게 리스트 조회
     */
    @Override
    public List<Object[]> getStoreList(String keyword, String rect) {

        //지하철역 검색
        List<String> stationNameList = queryFactory.select(subwayStation.stationName).from(subwayStation)
                .where(subwayStation.isUse.isTrue()).fetch();

        QCongestion subCongestion = new QCongestion("sub");

        List<Tuple> result = queryFactory
                .select(store ,store.heartList.size(), JPAExpressions
                        .select(subCongestion.congestionScore.avg())
                        .from(subCongestion)
                        .where(subCongestion.regDateTime.between(LocalDateTime.now().minusHours(2), LocalDateTime.now()),
                                subCongestion.store.storeId.eq(store.storeId))
                        .groupBy(store.storeId))
                .from(store)
                .leftJoin(store.businessHours).fetchJoin()
                .where(keywordSearch(keyword, stationNameList), inRect(rect))
                .orderBy(sort().stream().toArray(OrderSpecifier[]::new))
                .limit(40)
                .fetch();

        return result.stream().map(t -> t.toArray()).collect(Collectors.toList());
    }

    private BooleanBuilder inRect(String rect) {

        BooleanBuilder builder = new BooleanBuilder();

        if(rect != null && !rect.isEmpty()){
            String[] coordinateArr = rect.split(",");

            double topLatY = Double.parseDouble(coordinateArr[0]);
            double bottomLatY = Double.parseDouble(coordinateArr[1]);
            double leftLngX = Double.parseDouble(coordinateArr[2]);
            double rightLngX = Double.parseDouble(coordinateArr[3]);

            builder.and(store.latY.loe(topLatY));
            builder.and(store.latY.goe(bottomLatY));
            builder.and(store.lngX.goe(leftLngX));
            builder.and(store.lngX.loe(rightLngX));
        }
        return builder;
    }

    private List<OrderSpecifier<?>> sort() {

        List<OrderSpecifier<?>> orders = new ArrayList<>();

        //이미지 유무
        orders.add(new OrderSpecifier<>(Order.DESC, existImage()));

        //리뷰 많은 순
        orders.add(new OrderSpecifier<>(Order.DESC, store.reviewList.size()));

        return orders;
    }

    private BooleanExpression existImage() {

        return new CaseBuilder()
                .when(store.storeImageList.isNotEmpty()
                        .or(queryFactory.select().from(reviewImage)
                                .join(review).on(review.eq(reviewImage.review))
                                .where(review.store.eq(store)).exists()))
                .then(true)
                .otherwise(false);
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
                        .where(subCongestion.regDateTime.between(LocalDateTime.now().minusHours(2), LocalDateTime.now()),
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
                        .where(subCongestion.regDateTime.between(LocalDateTime.now().minusHours(2), LocalDateTime.now()),
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
                .where(subCongestion.regDateTime.between(LocalDateTime.now().minusHours(2), LocalDateTime.now()),
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

        QCongestion subCongestion = new QCongestion("sub");

        Tuple tuple = queryFactory.select(store, memberImage, JPAExpressions
                .select(subCongestion.congestionScore.avg())
                .from(subCongestion)
                .where(subCongestion.regDateTime.between(LocalDateTime.now().minusHours(2), LocalDateTime.now()),
                        subCongestion.store.storeId.eq(store.storeId)))
                .from(store)
                .leftJoin(store.businessHours).fetchJoin()
                .leftJoin(store.modMember).fetchJoin()
                .leftJoin(memberImage).on(memberImage.member.memberId.eq(store.modMember.memberId))
                .where(store.storeId.eq(storeId))
                .fetchOne();

        return tuple != null ? Optional.ofNullable(tuple.toArray()) : Optional.empty();
    }

    /**
     * 관리자단 카페 리스트 조회
     */
    @Override
    public Page<Object[]> getAllStoreList(String[] sggNms, String[] searchType, String keyword, Pageable pageable) {

        QCongestion subCongestion = new QCongestion("sub");

        JPAQuery<Tuple> query = queryFactory
                .select(store, store.reviewList.size(), JPAExpressions
                        .select(subCongestion.congestionScore.avg())
                        .from(subCongestion)
                        .where(subCongestion.regDateTime.between(LocalDateTime.now().minusHours(2), LocalDateTime.now()),
                                subCongestion.store.storeId.eq(store.storeId))
                        .groupBy(store.storeId),
                        storeMemo.memoId)
                .from(store)
                .leftJoin(storeMemo).on(storeMemo.store.storeId.eq(store.storeId))
                .where(sggNmFilter(sggNms), searchKeyword(searchType, keyword))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        //정렬
        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(Store.class, "store");
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        List<Tuple> results = query.fetch();

        //count 쿼리
        JPAQuery<Tuple> countQuery = queryFactory
                .select(store, store.reviewList.size())
                .from(store)
                .where(sggNmFilter(sggNms), searchKeyword(searchType, keyword));

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

    /**
     * 관리자단 회원별 내 카페 리스트 조회
     */
    @Override
    public List<Store> getMyStoreByMemberIdOfAdmin(Long memberId){

        return queryFactory.select(store)
                .from(store)
                .join(heart).on(heart.store.storeId.eq(store.storeId))
                .where(heart.member.memberId.eq(memberId))
                .fetch();
    }

    /**
     * 오늘 등록된 카페 개수
     */
    @Override
    public Long getRegisterCountOfToday() {

        return queryFactory
                .select(store.count())
                .from(store)
                .where(store.regDateTime.between(LocalDateTime.now().toLocalDate().atStartOfDay(),
                        LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59))))
                .fetchOne();
    }

    /**
     * 웹 - 카페 리스트 조회
     */
    @Override
    public List<Store> getStoreListOfWeb(String keyword) {

        //지하철역 검색
        List<String> stationNameList = queryFactory.select(subwayStation.stationName).from(subwayStation)
                .where(subwayStation.isUse.isTrue()).fetch();

        return queryFactory.select(store)
                .from(store)
                .leftJoin(store.businessHours).fetchJoin()
                .where(keywordSearch(keyword,stationNameList))
                .fetch();
    }

    /**
     * 웹 - 지역별 컨텐츠 추천 카페 리스트 조회
     */
    @Override
    public List<Store> getContentsStoreListOfWeb(String sggNm, ContentsType type) {

        return queryFactory.select(store)
                .from(store)
                .leftJoin(store.businessHours).fetchJoin()
                .join(contentsStore).on(contentsStore.store.eq(store))
                .where(store.address.sggNm.eq(sggNm), contentsStore.contentsType.eq(type))
                .fetch();
    }

    private BooleanBuilder sggNmFilter(String[] sggNms) {

        BooleanBuilder builder = new BooleanBuilder();

        if(sggNms != null){
            for(String sggNm : sggNms){
                builder.or(store.address.sggNm.eq(sggNm));
            }
        }
        return builder;
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

    private BooleanExpression containStoreId(String keyword) {

        Long storeId = null;

        try {
            storeId = Long.parseLong(keyword);
        }catch (NumberFormatException e){
            e.getMessage();
        }

        return !isEmpty(keyword) && storeId != null? store.storeId.eq(storeId) : null;

    }

    private BooleanBuilder containStoreName(String keyword) {

        BooleanBuilder builder = new BooleanBuilder();

        if(!isEmpty(keyword)){

            if(keyword.contains("투썸 플레이스")){
                keyword = keyword.replace("투썸 플레이스", "투썸");
            }else if (keyword.contains("스벅")){
                keyword = keyword.replace("스벅", "스타벅스");
            }

            String[] wordArr = keyword.split(" ");

            for(String word : wordArr){
                builder.and(store.storeName.contains(word));
            }
        }
        return builder;
    }

    private BooleanExpression containAddress(String keyword) {

        return !isEmpty(keyword) ? store.address.fullAddress.contains(keyword) : null;

    }

    private BooleanBuilder keywordSearch(String keyword, List<String> subwayStationNameList){
        BooleanBuilder builder = new BooleanBuilder();

        if (keyword != null && !keyword.isEmpty()){

            if(keyword.contains("투썸 플레이스")){
                keyword = keyword.replace("투썸 플레이스", "투썸");
            }else if (keyword.contains("스벅")){
                keyword = keyword.replace("스벅", "스타벅스");
            }

            String[] wordArr = keyword.split(" ");

            for(String word : wordArr){

                //"카페" 단어 통과
                if(word.equals("카페")){
                    continue;
                }

                boolean ctn = false;

                //구 검색
                for (String sgg : sggArr){
                    if (word.equals(sgg) || word.equals(sgg+"구")){
                        builder.and(store.address.sggNm.eq(sgg+"구"));

                        ctn = true;
                        break;
                    }
                }

                //구로 필터링 했을 경우
                if(ctn) continue;

                QSubwayStation subSubwayStation = new QSubwayStation("subSubwayStation");

                for(String stationName : subwayStationNameList){

                    String compareStationName = StringUtils.removeEnd(stationName,"입구");

                    if(word.equals(compareStationName) || word.equals(compareStationName + "역")
                            || word.equals(compareStationName + "입구") || word.equals(compareStationName + "입구역")){
                        //역 근처 카페 필터링
                        builder.and(store.storeId.in(JPAExpressions.select(nearStoreToSubwayStation.store.storeId)
                                .from(nearStoreToSubwayStation)
                                .join(subSubwayStation).on(subSubwayStation.eq(nearStoreToSubwayStation.subwayStation))
                                .where(subSubwayStation.isUse.isTrue(),
                                        subSubwayStation.stationName.eq(stationName))));

                        ctn = true;
                        break;
                    }
                }

                //지하철역으로 필터링 했을 경우
                if(ctn) continue;

                //카페명 검색
                builder.and(store.storeName.contains(word));
            }
        }
        return builder;
    }
}
