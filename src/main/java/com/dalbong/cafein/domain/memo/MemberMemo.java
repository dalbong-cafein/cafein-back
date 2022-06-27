package com.dalbong.cafein.domain.memo;

import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.store.Store;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@ToString(exclude = {"member"})
@DiscriminatorValue("member")
@Entity
public class MemberMemo extends Memo{

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public MemberMemo(Member member, Member writer, String content){
        super(writer, content);
        this.member = member;
    }

}
