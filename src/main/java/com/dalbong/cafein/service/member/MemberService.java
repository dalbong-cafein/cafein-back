package com.dalbong.cafein.service.member;

import com.dalbong.cafein.domain.member.AuthProvider;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.dto.admin.member.AdminDetailMemberResDto;
import com.dalbong.cafein.dto.admin.member.AdminMemberListResDto;
import com.dalbong.cafein.dto.admin.member.AdminMemberResDto;
import com.dalbong.cafein.dto.admin.member.AdminMemberUpdateDto;
import com.dalbong.cafein.dto.image.ImageDto;
import com.dalbong.cafein.dto.login.AccountUniteRegDto;
import com.dalbong.cafein.dto.member.MemberInfoDto;
import com.dalbong.cafein.dto.member.MemberUpdateDto;
import com.dalbong.cafein.dto.page.PageRequestDto;

import java.io.IOException;

public interface MemberService {

    void saveLoginHistory(Member member, AuthProvider authProvider, String ClientIp);

    Long uniteAccount(String email, AccountUniteRegDto accountUniteRegDto);

    Boolean isDuplicateNickname(String nickname);

    void modifyPhone(String phone, Long principalId);

    ImageDto modifyImageAndNickname(MemberUpdateDto memberUpdateDto, Long principalId) throws IOException;

    void leave(Long memberId);

    MemberInfoDto getMemberInfo(Long memberId);

    void modifyOfAdmin(AdminMemberUpdateDto adminMemberUpdateDto);

    AdminMemberListResDto getMemberListOfAdmin(PageRequestDto pageRequestDto);

    AdminDetailMemberResDto getDetailMemberOfAdmin(Long memberId);

    Long getRegisterCountOfToday();
}
