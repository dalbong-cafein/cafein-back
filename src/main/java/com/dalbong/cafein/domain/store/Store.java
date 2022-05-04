package com.dalbong.cafein.domain.store;

import com.dalbong.cafein.domain.BaseEntity;
import com.dalbong.cafein.domain.address.Address;
import com.dalbong.cafein.domain.businessHours.BusinessHours;
import com.dalbong.cafein.domain.heart.Heart;
import com.dalbong.cafein.domain.image.StoreImage;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.review.DetailEvaluation;
import com.dalbong.cafein.domain.review.Recommendation;
import com.dalbong.cafein.domain.review.Review;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"reviewList","storeImageList","heartList"})
@Entity
public class Store extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reg_member_id", nullable = false)
    private Member regMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mod_member_id", nullable = false)
    private Member modMember;

    @Column(nullable = false)
    private String storeName;

    @Embedded
    private Address address;

    private String website;

    private String phone;

    private int katechX;

    private int katechY;

    private Double lngX;

    private Double latY;

    //TODO data api에서 가게Id 필요 유무

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "businessHours")
    private BusinessHours businessHours;

    @BatchSize(size = 100)
    @Builder.Default
    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Review> reviewList = new ArrayList<>();

    @BatchSize(size = 100)
    @Builder.Default
    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Heart> heartList = new ArrayList<>();

    @BatchSize(size = 100)
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

    public void changeModMember(Member member){
        this.modMember = member;
    }

    public void changeBusinessHours(BusinessHours businessHours){
        this.businessHours = businessHours;
    }

    public double getRecommendPercent(){

        int totalSize = reviewList.size();

        double recommendCnt = 0;

        if(!reviewList.isEmpty()){
            for(Review review : reviewList){
                if(review.getRecommendation().equals(Recommendation.GOOD)){
                    recommendCnt += 1;
                }
            }
        }

        return (recommendCnt / totalSize) * 100;
    }

    public boolean checkIsOpen(){
        LocalDateTime now = LocalDateTime.now();
        LocalTime nowTime = now.toLocalTime();

        //현재 요일
       int dayOfWeekNumber = now.getDayOfWeek().getValue();

       boolean isOpen = false;

       switch(dayOfWeekNumber){
           case 0: //일
               isOpen = nowTime.isAfter(this.businessHours.getOnSun().getOpen()) && nowTime.isBefore(this.businessHours.getOnSun().getClosed());
               break ;
           case 1:
               isOpen = nowTime.isAfter(this.businessHours.getOnMon().getOpen()) && nowTime.isBefore(this.businessHours.getOnMon().getClosed());
               break ;
           case 2:
               isOpen = nowTime.isAfter(this.businessHours.getOnTue().getOpen()) && nowTime.isBefore(this.businessHours.getOnTue().getClosed());
               break ;
           case 3:
               isOpen = nowTime.isAfter(this.businessHours.getOnWed().getOpen()) && nowTime.isBefore(this.businessHours.getOnWed().getClosed());
               break ;
           case 4:
               isOpen = nowTime.isAfter(this.businessHours.getOnThu().getOpen()) && nowTime.isBefore(this.businessHours.getOnThu().getClosed());
               break ;
           case 5:
               isOpen = nowTime.isAfter(this.businessHours.getOnFri().getOpen()) && nowTime.isBefore(this.businessHours.getOnFri().getClosed());
               break ;
           case 6:
               isOpen = nowTime.isAfter(this.businessHours.getOnSat().getOpen()) && nowTime.isBefore(this.businessHours.getOnSat().getClosed());
               break ;
        }

        return isOpen;
    }
}
