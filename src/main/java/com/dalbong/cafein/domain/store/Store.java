package com.dalbong.cafein.domain.store;

import com.dalbong.cafein.domain.BaseEntity;
import com.dalbong.cafein.domain.address.Address;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.review.Review;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Entity
public class Store extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String storeName;

    private int americano;

    @Embedded
    private Address address;

    private String website;

    private String phone;

    @Column(nullable = false)
    private int mapX;

    @Column(nullable = false)
    private int mapY;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isApproval = false;

    @Column(nullable = false)
    private SocketCnt socketCnt;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "hash_tag", joinColumns = @JoinColumn(name = "store_id"))
    @Builder.Default
    @Column(name = "hash_tag")
    private Set<String> hashTagSet = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "feature_store",joinColumns = @JoinColumn(name = "store_id"))
    @Builder.Default
    @Column(name = "feature")
    private List<Feature> featureList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Review> reviewList = new ArrayList<>();

    public void changeIsApproval(){
        if (isApproval){
            isApproval = false;
        }else{
            isApproval = true;
        }
    }


}
