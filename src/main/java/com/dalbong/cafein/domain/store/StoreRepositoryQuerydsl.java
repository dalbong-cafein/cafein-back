package com.dalbong.cafein.domain.store;

import com.dalbong.cafein.domain.address.Address;
import com.dalbong.cafein.web.domain.contents.ContentsType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoreRepositoryQuerydsl {

    /**
     * 기존 카페 주소 등록 여부 확인
     */
    boolean existAddress(Address address);

    /**
     * 앱단 가게 리스트 조회
     */
    List<Object[]> getStoreList(String keyword, String rect);

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
     * 앱단 조회 카페 기준 근처 카페 리스트 조회
     */
    List<Object[]> getNearStoreListOfStore(@Param("storeId") Long storeId, @Param("latY") double latY, @Param("lngX") double lngX);

    /**
     * 앱단 카페 상세 페이지 조회
     */
    Optional<Object[]> getDetailStore(Long storeId);

    /**
     * 관리자단 전체 카페 리스트 조회
     */
    Page<Object[]> getAllStoreList(String[] sggNms, String[] searchType, String keyword, Pageable pageable);

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

    /**
     * 웹 - 지역별 컨텐츠 카페 추천 리스트 조회
     */
    List<Store> getContentsStoreListOfWeb(String sggNm, ContentsType type);

}
