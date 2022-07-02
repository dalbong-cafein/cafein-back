package com.dalbong.cafein.dto.admin.member;

import com.dalbong.cafein.domain.member.GenderType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminMemberUpdateDto {

    @NotNull
    private Long memberId;

    @NotNull
    private String phone;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate birth;

    @NotNull
    private GenderType genderType;
}
