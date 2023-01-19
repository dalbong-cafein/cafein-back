package com.dalbong.cafein.domain.store;

import com.dalbong.cafein.domain.address.Address;
import com.dalbong.cafein.domain.congestion.QCongestion;
import com.dalbong.cafein.domain.store.dto.StoreQueryDto;
import com.dalbong.cafein.domain.subwayStation.QSubwayStation;
import com.dalbong.cafein.domain.subwayStation.SubwayStation;
import com.dalbong.cafein.domain.university.QUniversity;
import com.dalbong.cafein.domain.university.University;
import com.dalbong.cafein.util.SqlFunctionUtil;
import com.dalbong.cafein.web.domain.contents.ContentsType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
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
import static com.dalbong.cafein.domain.nearStoreToUniversity.QNearStoreToUniversity.nearStoreToUniversity;
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
    public List<StoreQueryDto> getStoreList(String keyword, String centerCoordinates, String userCoordinates, String rect) {

        //대학교 리스트
        List<University> universityList = getUniversityList();

        //지하철역 리스트
        List<SubwayStation> subwayStationList = getSubwayStationList();

        //혼잡도 평균
        QCongestion subCongestion = new QCongestion("sub");
        JPQLQuery<Double> congestionAvg = JPAExpressions
                .select(subCongestion.congestionScore.avg())
                .from(subCongestion)
                .where(subCongestion.regDateTime.between(LocalDateTime.now().minusHours(2), LocalDateTime.now()),
                        subCongestion.store.storeId.eq(store.storeId))
                .groupBy(store.storeId);


        JPAQuery<StoreQueryDto> query;

        if(userCoordinates == null){
             query = queryFactory.select((Projections.constructor(StoreQueryDto.class, store, store.heartList.size(), congestionAvg)));

        }else {
            //사용자 위치와 카페 거리계산
            NumberExpression<Double> userDistance = SqlFunctionUtil.calculateDistance(userCoordinates);;

            query = queryFactory.select((Projections.constructor(StoreQueryDto.class, store, store.heartList.size(), congestionAvg, userDistance)));
        }

        return query
                .from(store)
                .leftJoin(store.businessHours).fetchJoin()
                .where(keywordSearch(keyword, universityList, subwayStationList), inRect(rect))
                .orderBy(sort(keyword, centerCoordinates, universityList, subwayStationList).stream().toArray(OrderSpecifier[]::new))
                .limit(40)
                .fetch();
    }

    private List<SubwayStation> getSubwayStationList() {

        QSubwayStation subSubwayStation = new QSubwayStation("subSubwayStation");

        return queryFactory.select(subSubwayStation)
                .from(subSubwayStation)
                .where(subSubwayStation.isUse.isTrue())
                .fetch();
    }

    private List<University> getUniversityList() {

        QUniversity subUniversity = new QUniversity("subUniversity");

        return queryFactory.select(subUniversity)
                .from(subUniversity)
                .where(subUniversity.isUse.isTrue())
                .fetch();
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

    private List<OrderSpecifier<?>> sort(String keyword, String centerCoordinates, List<University> universityList,
                                         List<SubwayStation> subwayStationList) {

        List<OrderSpecifier<?>> orders = new ArrayList<>();

        Optional<String> optSggNm = findSggNm(keyword);
        Optional<University> optUniversity = findUniversity(keyword, universityList);
        Optional<SubwayStation> optSubwayStation = findSubwayStation(keyword, subwayStationList);

        //구 관련 검색 정렬
        if(optSggNm.isPresent()){
            //이미지 유무
            orders.add(new OrderSpecifier<>(Order.DESC, existImage()));

            //리뷰 많은 순
            orders.add(new OrderSpecifier<>(Order.DESC, store.reviewList.size()));

        }
        //대학교 관련 검색 정렬
        else if(optUniversity.isPresent()){
            University university = optUniversity.get();
            NumberExpression<Double> distance = SqlFunctionUtil.calculateDistance(store.latY, store.lngX, university.getLatY(), university.getLngX());
            orders.add(distance.asc());

        }
        //지하철역 관련 검색 정렬
        else if(optSubwayStation.isPresent()){
            SubwayStation subwayStation = optSubwayStation.get();
            NumberExpression<Double> distance = SqlFunctionUtil.calculateDistance(store.latY, store.lngX, subwayStation.getLatY(), subwayStation.getLngX());
            orders.add(distance.asc());

        }
        //지도 좌표 관련 검색 정렬
        else if(centerCoordinates != null && !centerCoordinates.isEmpty()){

            NumberExpression<Double> distance = SqlFunctionUtil.calculateDistance(centerCoordinates);
            orders.add(distance.asc());

        }else{
            //이미지 유무
            orders.add(new OrderSpecifier<>(Order.DESC, existImage()));

            //리뷰 많은 순
            orders.add(new OrderSpecifier<>(Order.DESC, store.reviewList.size()));
        }

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

        return queryFactory.select(store)
                .from(store)
                .leftJoin(store.businessHours).fetchJoin()
                .where(keywordSearch(keyword, getUniversityList(), getSubwayStationList()))
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

            String relatedKeyword = relatedKeywordOfStoreName(keyword);

            String[] wordArr = relatedKeyword.split(" ");

            for(String word : wordArr){
                builder.and(store.storeName.contains(word));
            }
        }
        return builder;
    }

    private BooleanExpression containAddress(String keyword) {

        return !isEmpty(keyword) ? store.address.fullAddress.contains(keyword) : null;
    }

    private BooleanBuilder keywordSearch(String keyword, List<University> universityList, List<SubwayStation> subwayStationList){

        BooleanBuilder builder = new BooleanBuilder();

        if (keyword != null && !keyword.isEmpty()){

            //연관 키워드 처리
            String relatedKeyword = relatedKeywordOfStoreName(keyword);

            String[] wordArr = relatedKeyword.split(" ");

            for(String word : wordArr){

                //"카페" 단어 통과
                if(word.equals("카페")) continue;

                //구 검색
                if(searchSggNm(word, builder)) continue;

                //대학교로 검색
                if(searchUniversity(word, builder, universityList)) continue;

                //지하철역으로 검색
                if(searchSubwayStation(word, builder, subwayStationList)) continue;

                //카페명 검색
                builder.and(store.storeName.contains(word));
            }
        }
        return builder;
    }

    private String relatedKeywordOfStoreName(String keyword) {

        return keyword.replace("투썸 플레이스", "투썸")
                .replace("스벅", "스타벅스")
                .replace("이디야커피", "이디야");
    }

    private boolean searchSubwayStation(String word, BooleanBuilder builder, List<SubwayStation> subwayStationList) {

        Optional<SubwayStation> optSubwayStation = findSubwayStation(word, subwayStationList);

        if(optSubwayStation.isPresent()){
            //역 근처 카페 필터링
            builder.and(store.storeId.in(JPAExpressions.select(nearStoreToSubwayStation.store.storeId)
                    .from(nearStoreToSubwayStation)
                    .join(subwayStation).on(subwayStation.eq(nearStoreToSubwayStation.subwayStation))
                    .where(subwayStation.isUse.isTrue(),
                            subwayStation.stationName.eq(optSubwayStation.get().getStationName()))));
            return true;
        }
        return false;
    }

    private Optional<SubwayStation> findSubwayStation(String word, List<SubwayStation> subwayStationList){

        if(word != null && !word.isEmpty()){
            for(SubwayStation subwayStation : subwayStationList){

                String compareStationName = StringUtils.removeEnd(subwayStation.getStationName(),"입구");

                if(word.equals(compareStationName) || word.equals(compareStationName + "역")
                        || word.equals(compareStationName + "입구") || word.equals(compareStationName + "입구역")){

                    return Optional.of(subwayStation);
                }
            }
        }

        return Optional.empty();
    }

    private boolean searchUniversity(String word, BooleanBuilder builder, List<University> universityNameList) {

        Optional<University> optUniversity = findUniversity(word, universityNameList);

        if(optUniversity.isPresent()){
            //대학교 근처 카페 필터링
            builder.and(store.storeId.in(JPAExpressions.select(nearStoreToUniversity.store.storeId)
                    .from(nearStoreToUniversity)
                    .join(QUniversity.university).on(QUniversity.university.eq(nearStoreToUniversity.university))
                    .where(QUniversity.university.isUse.isTrue(),
                            QUniversity.university.universityName.eq(optUniversity.get().getUniversityName()))));
            return true;
        }
        return false;
    }

    private Optional<University> findUniversity(String word, List<University> universityList){

        if(word != null && !word.isEmpty()){
            for(University university : universityList){

                String compareUniversityName = university.getUniversityName().replace("대학교","대");

                StringUtils.removeEnd(compareUniversityName, "학");

                //대학교 연관 키워드 처리
                String relatedWord = relatedKeywordOfUniversity(word);

                if(relatedWord.equals(compareUniversityName) || relatedWord.equals(compareUniversityName+"학교")){
                    return Optional.of(university);
                }
            }
        }
        return Optional.empty();
    }

    private String relatedKeywordOfUniversity(String word) {

        return word.replace("외대", "외국어대")
                .replace("여대", "여자대")
                .replace("체대", "체육대")
                .replace("홍대","홍익대")
                .replace("이대","이화여자대")
                .replace("건대","건국대")
                .replace("고대", "고려대")
                .replace("연대","연세대")
                .replace("중대","중앙대")
                .replace("서울과기대", "서울과학기술대")
                .replace("과기대","서울과학기술대")
                .replace("장신대", "장로회신학대")
                .replace("중대","중앙대")
                .replace("감신대","감리교신학대")
                .replace("서울교대","서울교육대")
                .replace("교대", "서울교육대")
                .replace("한예종","한국예술종합")
                .replace("숙대","숙명여자대")
                .replace("한국방통대","한국방송통신대")
                .replace("방통대", "한국방송통신대")
                .replace("성대", "성균관대")
                .replace("동대", "동국대");
    }

    private boolean searchSggNm(String word, BooleanBuilder builder) {

        Optional<String> optSggNm = findSggNm(word);

        if(optSggNm.isPresent()){
            //구 카페 필터링링
            builder.and(store.address.sggNm.eq(optSggNm.get()+"구"));
            return true;
        }
        return false;
    }


    private Optional<String> findSggNm(String word){

        if(word != null && !word.isEmpty()){
            for (String sgg : sggArr){
                if (word.equals(sgg) || word.equals(sgg+"구")){
                    return Optional.of(sgg);
                }
            }
        }
        return Optional.empty();
    }
}
