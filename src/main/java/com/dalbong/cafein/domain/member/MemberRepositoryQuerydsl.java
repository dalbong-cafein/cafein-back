package com.dalbong.cafein.domain.member;

import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.dto.admin.member.AdminDetailMemberResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepositoryQuerydsl {

    Optional<Member> findByKakaoIdAndNotLeave(String kakaoId);

    Optional<Member> findByNaverIdAndNotLeave(String naverId);

    Optional<Member> findByAppleIdAndNotLeave(String appleId);

    /**
     * 닉네임 중복확인
     */
    boolean existNickname(String nickname);

    /**
     * 관리자단 전체 회원 리스트 조회
     */
    Page<Object[]> getAllMemberListOfAdmin(String[] searchType, String keyword, Pageable pageable);

    /**
     * 관리자단 회원 상세 조회
     */
    Optional<Object[]> getDetailMemberOfAdmin(Long memberId);

    /**
     * 관리자단 오늘 등록된 회원 수 조회
     */
    Long getRegisterCountOfToday();

    /**
     * 회원 정지기간 만료 회원 리스트 조회
     */
    List<Member> findByReportExpiredToday();

}
