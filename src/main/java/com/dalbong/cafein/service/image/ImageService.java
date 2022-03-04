package com.dalbong.cafein.service.image;

import com.dalbong.cafein.domain.image.Image;
import com.dalbong.cafein.domain.member.Member;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    Image saveMemberImage(Member member, MultipartFile imageFile);

    void remove(Member member);


}
