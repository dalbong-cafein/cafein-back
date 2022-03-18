package com.dalbong.cafein.service.member;

import com.dalbong.cafein.domain.member.AuthProvider;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class MemberServiceImplTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void before(){
        Member member = createMember("testUsername", "testNickname", "010-1111-1111",
                "testEmail@naver.com", "asdf123", LocalDate.now());
        this.member = member;
    }


    @Test
    void 휴대폰번호_수정() throws Exception{
        //given

        //when
        memberService.modifyPhone("010-0000-0000",member.getMemberId());

        //then
        Member findMember = memberRepository.findById(this.member.getMemberId()).get();

        assertThat(findMember.getPhone()).isEqualTo("010-0000-0000");
    }

    @Test
    void 닉네임_중복확인_사용가능o() throws Exception{
        //given
        //when
        Boolean result = memberService.isDuplicateNickname("사용가능한 닉네임");

        //then
        assertThat(result).isTrue();
    }


    @Test
    void 닉네임_중복확인_사용가능x() throws Exception{
        //given
        //when
        Boolean result = memberService.isDuplicateNickname(member.getNickname());

        //then
        assertThat(result).isFalse();
    }

    private Member createMember(String username, String nickname, String phone, String email,
                                String kakaoId, LocalDate birth) {

        Member member = Member.builder()
                .username(username)
                .nickname(nickname)
                .phone(phone)
                .email(email)
                .password("1111")
                .birth(birth)
                .kakaoId(kakaoId)
                .mainAuthProvider(AuthProvider.KAKAO)
                .build();

        return memberRepository.save(member);
    }





}