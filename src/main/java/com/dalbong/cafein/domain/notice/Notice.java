package com.dalbong.cafein.domain.notice;

import com.dalbong.cafein.domain.BaseEntity;
import com.dalbong.cafein.domain.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@ToString(exclude = {"toMember"})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Entity
public abstract class Notice extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_member_id")
    private Member toMember;

    private String content;

    private boolean isRead;


    public Notice(Member toMember, String content){
        this.toMember = toMember;
        this.content = content;
        this.isRead = false;
    }

}
