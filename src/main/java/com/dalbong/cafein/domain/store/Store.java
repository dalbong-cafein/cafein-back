package com.dalbong.cafein.domain.store;

import com.dalbong.cafein.domain.BaseEntity;
import com.dalbong.cafein.domain.address.Address;
import com.dalbong.cafein.domain.businessHours.BusinessHours;
import com.dalbong.cafein.domain.heart.Heart;
import com.dalbong.cafein.domain.image.StoreImage;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.review.Recommendation;
import com.dalbong.cafein.domain.review.Review;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

    private String wifiPassword;

    private int katechX;

    private int katechY;

    private Double lngX;

    private Double latY;

    //TODO data api에서 가게Id 필요 유무

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "businessHours")
    private BusinessHours businessHours;

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

    public void changeModMember(Member member){
        this.modMember = member;
    }

    public void changeBusinessHours(BusinessHours businessHours){
        this.businessHours = businessHours;
    }

    public Double getRecommendPercent(){

        int totalSize = reviewList.size();

        //추천 데이터가 없을 경우
        if (totalSize == 0){
            return null;
        }

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

    public Boolean checkIsOpen(){

        //영업시간 데이터 없는 경우
        if(this.businessHours == null){
            return null;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalTime nowTime = now.toLocalTime();

        //현재 요일
       int dayOfWeekNumber = now.getDayOfWeek().getValue();

       boolean isOpen = false;

       //영업중 체크
       switch(dayOfWeekNumber){
           case 0: //일
               if(this.businessHours.getOnSun() == null){
                   return null;
               }
               isOpen = nowTime.isAfter(this.businessHours.getOnSun().getOpen()) && nowTime.isBefore(this.businessHours.getOnSun().getClosed());
               break ;
           case 1:
               if(this.businessHours.getOnMon() == null){
                   return null;
               }
               isOpen = nowTime.isAfter(this.businessHours.getOnMon().getOpen()) && nowTime.isBefore(this.businessHours.getOnMon().getClosed());
               break ;
           case 2:
               if(this.businessHours.getOnTue() == null){
                   return null;
               }
               isOpen = nowTime.isAfter(this.businessHours.getOnTue().getOpen()) && nowTime.isBefore(this.businessHours.getOnTue().getClosed());
               break ;
           case 3:
               if(this.businessHours.getOnWed() == null){
                   return null;
               }
               isOpen = nowTime.isAfter(this.businessHours.getOnWed().getOpen()) && nowTime.isBefore(this.businessHours.getOnWed().getClosed());
               break ;
           case 4:
               if(this.businessHours.getOnThu() == null){
                   return null;
               }
               isOpen = nowTime.isAfter(this.businessHours.getOnThu().getOpen()) && nowTime.isBefore(this.businessHours.getOnThu().getClosed());
               break ;
           case 5:
               if(this.businessHours.getOnFri() == null){
                   return null;
               }
               isOpen = nowTime.isAfter(this.businessHours.getOnFri().getOpen()) && nowTime.isBefore(this.businessHours.getOnFri().getClosed());
               break ;
           case 6:
               if(this.businessHours.getOnSat() == null){
                   return null;
               }
               isOpen = nowTime.isAfter(this.businessHours.getOnSat().getOpen()) && nowTime.isBefore(this.businessHours.getOnSat().getClosed());
               break ;
        }

        return isOpen;
    }
}
