package com.dalbong.cafein.service;

import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    /**
     * 닉네임 중복체크
     */
    @Transactional
    @Override
    public Boolean isDuplicateNickname(String nickname) {

        Optional<Member> result = memberRepository.findByNickname(nickname);

        return result.isEmpty();
    }
}
