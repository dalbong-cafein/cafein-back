package com.dalbong.cafein.service.image;

import com.dalbong.cafein.domain.image.Image;
import com.dalbong.cafein.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Transactional
@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService{

    /**
     * 프로필 이미지 저장
     */
    @Transactional
    @Override
    public Image saveMemberImage(Member member, MultipartFile imageFile) {
        return null;
    }

    /**
     * 이미지 삭제
     */
    @Transactional
    @Override
    public void remove(Member member) {

    }
}
