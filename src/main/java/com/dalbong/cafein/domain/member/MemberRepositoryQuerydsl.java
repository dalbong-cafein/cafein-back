package com.dalbong.cafein.domain.member;

import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.dto.admin.member.AdminDetailMemberResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepositoryQuerydsl {

    Optional<Member> findByKakaoIdAndNotDeleted(String kakaoId);

    Optional<Member> findByNaverIdAndNotDeleted(String naverId);

    /**
     * 관리자단 전체 회원 리스트 조회
     */
    Page<Object[]> getAllMemberListOfAdmin(String[] searchType, String keyword, Pageable pageable);

    /**
     * 관리자단 회원 상세 조회
     */
    Optional<Object[]> getDetailMemberOfAdmin(Long memberId);
}
