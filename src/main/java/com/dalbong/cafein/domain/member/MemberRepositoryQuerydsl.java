package com.dalbong.cafein.domain.member;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepositoryQuerydsl {

    Optional<Member> findByKakaoIdAndNotDeleted(String kakaoId);

    Optional<Member> findByNaverIdAndNotDeleted(String naverId);

}
