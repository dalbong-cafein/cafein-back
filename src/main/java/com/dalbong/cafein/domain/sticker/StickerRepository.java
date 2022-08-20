package com.dalbong.cafein.domain.sticker;

import com.dalbong.cafein.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StickerRepository extends JpaRepository<Sticker, Long>, StickerRepositoryQuerydsl {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from Sticker s where s.member =:member")
    void deleteByMember(@Param("member") Member member);

    @Query("select count(s) from Sticker s where s.member.memberId =:memberId")
    int getCountStickerOfMember(@Param("memberId") Long memberId);

    @Query("select s from Sticker s where s.member.memberId =:memberId order by s.stickerId desc ")
    List<Sticker> getStickerList(@Param("memberId") Long memberId);
}
