package com.dalbong.cafein.web.domain.contents;

import com.dalbong.cafein.domain.store.Store;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"store"})
@Entity
public class ContentsStore {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contentsStoreId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Enumerated(EnumType.STRING)
    private ContentsType contentsType;
}
