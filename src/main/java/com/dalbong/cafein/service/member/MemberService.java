package com.dalbong.cafein.service.member;

import com.dalbong.cafein.dto.admin.member.AdminDetailMemberResDto;
import com.dalbong.cafein.dto.admin.member.AdminMemberListResDto;
import com.dalbong.cafein.dto.admin.member.AdminMemberResDto;
import com.dalbong.cafein.dto.login.AccountUniteRegDto;
import com.dalbong.cafein.dto.member.MemberInfoDto;
import com.dalbong.cafein.dto.member.MemberUpdateDto;
import com.dalbong.cafein.dto.page.PageRequestDto;

import java.io.IOException;

public interface MemberService {

    Long uniteAccount(String email, AccountUniteRegDto accountUniteRegDto);

    Boolean isDuplicateNickname(String nickname);

    void modifyPhone(String phone, Long principalId);

    void modifyImageAndNickname(MemberUpdateDto memberUpdateDto, Long principalId) throws IOException;

    void leave(Long memberId);

    MemberInfoDto getMemberInfo(Long memberId);

    AdminMemberListResDto getMemberListOfAdmin(PageRequestDto pageRequestDto);

    AdminDetailMemberResDto getDetailMemberOfAdmin(Long memberId);
}
