package com.dalbong.cafein.domain.image;

import com.dalbong.cafein.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberImageRepository extends JpaRepository<MemberImage,Long> {

    Optional<MemberImage> findByMember(Member member);
}
