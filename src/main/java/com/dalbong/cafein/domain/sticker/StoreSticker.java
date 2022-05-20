package com.dalbong.cafein.domain.sticker;

import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.store.Store;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"store"})
@DiscriminatorValue("store")
@Entity
public class StoreSticker extends Sticker{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = true)
    private Store store;


}
