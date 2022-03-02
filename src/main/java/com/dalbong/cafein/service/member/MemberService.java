package com.dalbong.cafein.service.member;

public interface MemberService {

    Boolean isDuplicateNickname(String nickname);

    void modifyPhone(String phone, Long principalId);
}
