package com.dalbong.cafein.dto.member;

import com.dalbong.cafein.dto.image.ImageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberUpdateDto {

    @NotBlank
    private String nickname;

    private MultipartFile imageFile;

    private Long deleteImageId;
}
