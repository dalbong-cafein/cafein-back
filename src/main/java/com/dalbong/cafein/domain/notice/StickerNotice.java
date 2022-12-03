package com.dalbong.cafein.domain.notice;

import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.sticker.Sticker;
import com.dalbong.cafein.domain.store.Store;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@ToString(exclude = {"sticker"})
@DiscriminatorValue("sticker")
@Entity
public class StickerNotice extends Notice{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sticker_id")
    private Sticker sticker;

    public StickerNotice(Sticker sticker, Member toMember){
        super(toMember,"스티커 증정 완료 | " + sticker.getStoreName());
        this.sticker = sticker;
    }

    public StickerNotice(Sticker sticker, Member toMember, String content){
        super(toMember, content);
        this.sticker = sticker;
    }
}
