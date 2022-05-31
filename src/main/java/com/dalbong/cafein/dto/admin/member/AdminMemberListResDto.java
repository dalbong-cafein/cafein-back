package com.dalbong.cafein.dto.admin.member;

import com.dalbong.cafein.dto.page.PageResultDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminMemberListResDto {

    private long memberCnt;

    private PageResultDTO<AdminMemberResDto, Object[]> memberResDtoList;

}
