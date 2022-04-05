package com.dalbong.cafein.domain.store;

import com.dalbong.cafein.domain.BaseEntity;
import com.dalbong.cafein.domain.address.Address;
import com.dalbong.cafein.domain.heart.Heart;
import com.dalbong.cafein.domain.image.StoreImage;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.review.DetailEvaluation;
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
@ToString(exclude = {"hashTagSet","reviewList","reviewList","storeImageList"})
@Entity
public class Store extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    @Column(nullable = false)
    private String storeName;

    private Integer americano;

    @Embedded
    private Address address;

    private String website;

    private String phone;

    @Column(nullable = false)
    private int katechX;

    @Column(nullable = false)
    private int katechY;

    private Double lngX;

    private Double latY;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isApproval = false;

    @Embedded
    private DetailEvaluation detailEvaluation;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "hash_tag", joinColumns = @JoinColumn(name = "store_id"))
    @Builder.Default
    @Column(name = "hash_tag")
    private Set<String> hashTagSet = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Review> reviewList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Heart> heartList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "store",fetch = FetchType.LAZY)
    private List<StoreImage> storeImageList = new ArrayList<>();

    public void changePhone(String phone){
        this.phone = phone;
    }

    public void changeLatAndLng(Double lngX, Double latY){
        this.lngX = lngX;
        this.latY = latY;
    }

    public void changeIsApproval(){
        if (isApproval){
            isApproval = false;
        }else{
            isApproval = true;
        }
    }


}
