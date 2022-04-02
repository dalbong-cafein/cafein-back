package com.dalbong.cafein.domain.image;

import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.store.Store;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@ToString(exclude = {"store"})
@DiscriminatorValue("store")
@Entity
public class StoreImage extends Image{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    protected StoreImage(){}

    public StoreImage(Store store, String imageUrl){
        this.store = store;
    }

}
