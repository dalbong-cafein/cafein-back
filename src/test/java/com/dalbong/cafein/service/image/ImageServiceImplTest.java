package com.dalbong.cafein.service.image;

import com.dalbong.cafein.domain.image.Image;
import com.dalbong.cafein.domain.image.ImageRepository;
import com.dalbong.cafein.domain.image.MemberImage;
import com.dalbong.cafein.domain.image.MemberImageRepository;
import com.dalbong.cafein.domain.member.AuthProvider;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.member.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ImageServiceImplTest {
    
    @Autowired ImageService imageService;
    @Autowired MemberRepository memberRepository;
    @Autowired ImageRepository imageRepository;
    @Autowired MemberImageRepository memberImageRepository;

    /**
     * 회원 이미지 저장
     */
    @Test
    void 회원이미지_저장() throws Exception{
        //given
        Member member = createMember("testUsername", "testNickname", "010-0000-0000",
                "test@naver.com", "asdf123");

        MultipartFile imageFile = createImage("updateFile", "updateFilename.jpeg");

        //when
        Image image = imageService.saveMemberImage(member, imageFile);

        //then
        MemberImage memberImage = memberImageRepository.findById(image.getImageId()).get();

        Assertions.assertThat(image.getImageId()).isNotNull();
        Assertions.assertThat(image.getImageUrl()).isNotNull();
        Assertions.assertThat(memberImage.getMember().getMemberId()).isEqualTo(member.getMemberId());
        System.out.println(image.getImageUrl());
    }
    
    @Test
    void 회원이미지_삭제() throws Exception{
        //given
        
        //when
        //then
    }

    private MultipartFile createImage(String name, String originalFilename) {
        return new MockMultipartFile(name, originalFilename, "image/jpeg", "some-image".getBytes());
    }

    private Member createMember(String username, String nickname, String phone, String email,
                                String oauthId) {

        Member member = Member.builder()
                .username(username)
                .nickname(nickname)
                .phone(phone)
                .email(email)
                .password("1111")
                .oauthId(oauthId)
                .provider(AuthProvider.KAKAO)
                .build();

        return memberRepository.save(member);
    }
}