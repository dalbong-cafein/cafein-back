package com.dalbong.cafein.service.member;

import com.dalbong.cafein.domain.image.MemberImage;
import com.dalbong.cafein.domain.image.MemberImageRepository;
import com.dalbong.cafein.domain.member.AuthProvider;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.member.MemberRepository;
import com.dalbong.cafein.dto.member.MemberUpdateDto;
import com.dalbong.cafein.service.image.ImageService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class MemberServiceImplTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired ImageService imageService;
    @Autowired MemberImageRepository memberImageRepository;

    private Member member;

    @BeforeEach
    void before() throws IOException {
        Member member = createMember("testUsername", "testNickname", "010-1111-1111",
                "testEmail@naver.com", "asdf123", LocalDate.now());
        this.member = member;

        //회원 프로필 이미지 생성
        MultipartFile imageFile = createImage("testProfileImage", "testProfileImageFilename1.png");
        imageService.saveMemberImage(member, imageFile);
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

    /**
     * 프로필 사진, 닉네임 변경
     */
    @Test
    void 프로필사진_닉네임_수정() throws Exception{
        //given

        MultipartFile imageFile = createImage("updateProfileImage", "updateProfileImageFilename.jpeg");

        MemberUpdateDto memberUpdateDto = createMemberUpdateDto("updateNickname", imageFile);

        //when
        memberService.modifyImageAndNickname(memberUpdateDto, member.getMemberId());

        //then
        Member findMember = memberRepository.findById(this.member.getMemberId()).get();

        //닉네임 검증
        assertThat(findMember.getNickname()).isEqualTo(memberUpdateDto.getNickname());

        //프로필 이미지 검증
        MemberImage memberImage = memberImageRepository.findByMember(member).get();
        assertThat(memberImage.getIsSocial()).isFalse();
        System.out.println(memberImage.getImageUrl());

        System.out.println(findMember);

    }

    private MemberUpdateDto createMemberUpdateDto(String nickname, MultipartFile imageFile) {
        return new MemberUpdateDto(nickname, imageFile);
    }

    private MultipartFile createImage(String name, String originalFilename) {
        return new MockMultipartFile(name, originalFilename, "image/jpeg", "some-image".getBytes());
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