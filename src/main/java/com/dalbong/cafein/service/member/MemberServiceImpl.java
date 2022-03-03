package com.dalbong.cafein.service.member;

import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.member.MemberRepository;
import com.dalbong.cafein.dto.member.MemberUpdateDto;
import com.dalbong.cafein.handler.exception.CustomException;
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
    @Transactional(readOnly = true)
    @Override
    public Boolean isDuplicateNickname(String nickname) {

        Optional<Member> result = memberRepository.findByNickname(nickname);

        return result.isEmpty();
    }

    /**
     * 휴대폰 번호 변경
     */
    @Transactional
    @Override
    public void modifyPhone(String phone, Long principalId) {

        Member member = memberRepository.findById(principalId).orElseThrow(() ->
                new CustomException("존재하는 회원이 아닙니다."));

        member.changePhone(phone);
    }

    /**
     * 프로필 사진, 닉네임 변경
     */
    @Transactional
    @Override
    public void modifyImageAndNickname(MemberUpdateDto memberUpdateDto, Long principalId) {

        Member member = memberRepository.findById(principalId).orElseThrow(() ->
                new CustomException("존재하는 회원이 아닙니다."));

        member.changeNickname(memberUpdateDto.getNickname());
        //TODO 이미지 수정 필요

    }
}
