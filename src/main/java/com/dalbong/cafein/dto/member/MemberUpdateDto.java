package com.dalbong.cafein.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberUpdateDto {

    private String nickname;

    private MultipartFile imageFile;

}
