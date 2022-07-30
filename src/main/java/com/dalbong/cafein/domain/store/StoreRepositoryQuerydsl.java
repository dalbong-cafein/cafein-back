package com.dalbong.cafein.domain.store;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface StoreRepositoryQuerydsl {

    /**
     * 앱단 가게 리스트 조회
     */
    List<Object[]> getStoreList(String keyword);

    /**
     * 앱단 내 카페 리스트 조회
     */
    List<Object[]> getMyStoreList(Long principalId);

    /**
     * 엡단 내 가게 리스트 개수 지정 조회
     */
    List<Object[]> getCustomLimitMyStoreList(int limit, Long principalId);

    /**
     * 앱단 본인이 등록한 가게 리스트 조회
     */
    List<Object[]> getMyRegisterStoreList(Long principalId);

    /**
     * 앱단 카페 상세 페이지 조회
     */
    Optional<Object[]> getDetailStore(Long storeId);

    /**
     * 추천 검색 카페 리스트 조회
     */
    List<Store> getRecommendSearchStoreList(String keyword);


    /**
     * 관리자단 전체 카페 리스트 조회
     */
    Page<Object[]> getAllStoreList(String[] searchType, String keyword, Pageable pageable);

    /**
     * 관리자단 카페 상세 페이지 조회
     */
    Optional<Object[]> getDetailStoreOfAdmin(Long storeId);

    /**
     * 관리자단 회원별 내 카페 리스트 조회
     */
    List<Store> getMyStoreByMemberIdOfAdmin(Long memberId);

    /**
     * 오늘 등록된 카페 개수 조회
     */
    Long getRegisterCountOfToday();

    /**
     * 웹 - 카페 리스트 조회
     */
    List<Store> getStoreListOfWeb(String keyword);

}
