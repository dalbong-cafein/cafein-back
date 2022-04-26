package com.dalbong.cafein.domain.congestion;

import com.dalbong.cafein.domain.BaseEntity;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.store.Store;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"member","store"})
@Entity
public class Congestion extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long congestionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private CongestionType type;

}
