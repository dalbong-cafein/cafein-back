package com.dalbong.cafein.domain.heart;

import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.store.Store;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"member","store"})
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name="heart_uk",
                        columnNames={"member_id", "store_id"}
                )
        }
)
@Entity
public class Heart {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long heartId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    //연관관계 메서드
    public void setStore(Store store){
        this.store = store;
        store.getHeartList().add(this);
    }
}
