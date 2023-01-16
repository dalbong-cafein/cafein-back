package com.dalbong.cafein.domain.sticker;

import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.store.Store;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@NoArgsConstructor
@Getter
@ToString
@DiscriminatorValue("event")
@Entity
public class EventSticker extends Sticker{

    public EventSticker(Member member){
        super(member, "카페인");
    }
}
