package com.dalbong.cafein.domain.sticker;

import com.dalbong.cafein.domain.congestion.Congestion;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.review.Review;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@ToString(exclude = {"congestion"})
@DiscriminatorValue("congestion")
@Entity
public class CongestionSticker extends Sticker{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "congestion_id", nullable = true)
    private Congestion congestion;

    public CongestionSticker(Congestion congestion, Member member){
        super(member);
        this.congestion = congestion;
    }
}
