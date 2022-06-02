package com.dalbong.cafein.domain.memo;

import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.store.Store;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@ToString(exclude = {"store"})
@DiscriminatorValue("store")
@Entity
public class StoreMemo extends Memo{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    public StoreMemo(Store store, Member writer, String content){
        super(writer, content);
        this.store = store;
    }

}
