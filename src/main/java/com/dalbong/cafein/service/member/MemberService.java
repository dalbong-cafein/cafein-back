package com.dalbong.cafein.service.member;

import com.dalbong.cafein.dto.login.AccountUniteRegDto;
import com.dalbong.cafein.dto.member.MemberUpdateDto;

public interface MemberService {

    Long uniteAccount(String email, AccountUniteRegDto accountUniteRegDto);

    Boolean isDuplicateNickname(String nickname);

    void modifyPhone(String phone, Long principalId);

    void modifyImageAndNickname(MemberUpdateDto memberUpdateDto, Long principalId);
}
