package com.dalbong.cafein.domain.notice;

import com.dalbong.cafein.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice,Long> {

    @Query("select n from Notice n where n.toMember.memberId =:memberId order by n.noticeId desc")
    List<Notice> getNoticeList(@Param("memberId") Long principalId);

    @Modifying
    @Query("delete from Notice n where n.toMember.memberId =:memberId")
    void deleteByMemberId(@Param("memberId") Long memberId);

}
