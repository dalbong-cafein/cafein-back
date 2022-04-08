package com.dalbong.cafein.domain.store;

import com.dalbong.cafein.domain.BaseEntity;
import com.dalbong.cafein.domain.address.Address;
import com.dalbong.cafein.domain.businessHours.BusinessHours;
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
@ToString(exclude = {"reviewList","reviewList","storeImageList","heartList"})
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

    private int katechX;

    private int katechY;

    private Double lngX;

    private Double latY;

    //TODO data api에서 가게Id 필요 유무

    @Builder.Default
    @Column(nullable = false)
    private Boolean isApproval = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "businessHours")
    private BusinessHours businessHours;

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

    public void changeBusinessHours(BusinessHours businessHours){
        this.businessHours = businessHours;
    }
}
