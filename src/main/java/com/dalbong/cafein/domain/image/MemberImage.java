package com.dalbong.cafein.domain.image;

import com.dalbong.cafein.domain.member.Member;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@ToString(exclude = {"member"})
@DiscriminatorValue("member")
@Entity
public class MemberImage extends Image {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private Boolean isSocial;

    protected MemberImage(){}

    public MemberImage(Member member, String imagerUrl, Boolean isSocial){
        super(member.getMemberId(), imagerUrl);
        this.member = member;
        this.isSocial = isSocial;
    }

    public MemberImage(Member member, String imagerUrl){
        super(member.getMemberId(), imagerUrl);
        this.member = member;
        this.isSocial = false;
    }

}
