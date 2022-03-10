package com.dalbong.cafein.service.image;

import com.dalbong.cafein.domain.image.Image;
import com.dalbong.cafein.domain.image.ImageRepository;
import com.dalbong.cafein.domain.image.MemberImage;
import com.dalbong.cafein.domain.image.MemberImageRepository;
import com.dalbong.cafein.domain.member.AuthProvider;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.member.MemberRepository;
import com.dalbong.cafein.s3.S3Uploader;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ImageServiceImplTest {
    
    @Autowired ImageService imageService;
    @Autowired MemberRepository memberRepository;
    @Autowired ImageRepository imageRepository;
    @Autowired MemberImageRepository memberImageRepository;
    @Autowired S3Uploader s3Uploader;

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
    }
    
    @Test
    void 회원이미지_삭제() throws Exception{
        //given
        Member member = createMember("testUsername", "testNickname",
                "010-0000-0000", "testEmail@naver.com","asd1233");

        MultipartFile imageFile = createImage("updateFile", "testFilename.jpeg");
        String imageUrl = s3Uploader.s3Upload(imageFile);

        MemberImage memberImage = createMemberImage(member, imageUrl);

        Image findImage = imageRepository.findById(memberImage.getImageId()).get();

        //when
        imageService.remove(findImage);

        //then
        NoSuchElementException e1 = assertThrows(NoSuchElementException.class,
                () -> (imageRepository.findById(memberImage.getImageId())).get());

        NoSuchElementException e2 = assertThrows(NoSuchElementException.class,
                () -> (memberImageRepository.findById(memberImage.getImageId())).get());

        assertThat(e1.getMessage()).isEqualTo("No value present");
        assertThat(e2.getMessage()).isEqualTo("No value present");
    }

    private MemberImage createMemberImage(Member member, String imageUrl) {
        MemberImage memberImage = new MemberImage(member, imageUrl);
        return memberImageRepository.save(memberImage);
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