package com.dalbong.cafein.domain.image;

import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.store.Store;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@ToString(exclude = {"store","regMember"})
@DiscriminatorValue("store")
@Entity
public class StoreImage extends Image{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reg_member_id")
    private Member regMember;

    private Boolean isCafein;

    protected StoreImage(){}

    public StoreImage(Store store, Member regMember, String imageUrl){
        super(imageUrl);
        this.store = store;
        this.regMember = regMember;
        this.isCafein = false;
    }

    public StoreImage(Store store, Member regMember, String imageUrl, boolean isCafein){
        this(store, regMember, imageUrl);
        this.isCafein = isCafein;
    }
}
