package com.dalbong.cafein.domain.store;

import com.dalbong.cafein.domain.BaseEntity;
import com.dalbong.cafein.domain.address.Address;
import com.dalbong.cafein.domain.businessHours.BusinessHours;
import com.dalbong.cafein.domain.heart.Heart;
import com.dalbong.cafein.domain.image.StoreImage;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.nearStoreToSubwayStation.NearStoreToSubwayStation;
import com.dalbong.cafein.domain.review.Recommendation;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.store.dto.NearStoreDto;
import com.dalbong.cafein.web.domain.Recommend;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@NamedNativeQuery(name = "recommendNearStore",
        query = "select *, (6371*acos(cos(radians(:latY))*cos(radians(s.latY))*cos(radians(s.lngX) " +
                "-radians(:lngX))+sin(radians(:latY))*sin(radians(s.latY)))) AS distance " +
                "from store s " +
                "where s.store_id <> :storeId " +
                "having distance < 0.5 " +
                "order by distance",
        resultSetMapping = "recommendNearStore")
@SqlResultSetMapping(
        name = "recommendNearStore",
        classes = {@ConstructorResult(
                targetClass = NearStoreDto.class,
                columns = {
                        @ColumnResult(name = "store", type = Store.class),
                        @ColumnResult(name="distance", type = Double.class)})}
       )
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"regMember","modMember","reviewList","storeImageList","heartList","nearStoreToSubwayStationArrayList"})
@Entity
public class Store extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reg_member_id")
    private Member regMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mod_member_id")
    private Member modMember;

    @Column(nullable = false)
    private String storeName;

    @Column(nullable = false)
    @Embedded
    private Address address;

    private String website;

    private String phone;

    private String wifiPassword;

    private Double lngX;

    private Double latY;

    @Builder.Default
    private int viewCnt = 0;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "businessHours")
    private BusinessHours businessHours;

    @Builder.Default
    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    private List<Review> reviewList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Heart> heartList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "store",fetch = FetchType.LAZY)
    private List<StoreImage> storeImageList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "store",fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<NearStoreToSubwayStation> nearStoreToSubwayStationArrayList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Recommend> recommendList = new ArrayList<>();

    public void changeStoreName(String storeName){
        this.storeName = storeName;
    }

    public void changeAddress(Address address){
        this.address = address;
    }

    public void changePhone(String phone){
        this.phone = phone;
    }

    public void changeWebsite(String website){
        this.website = website;
    }

    public void changeWifiPassword(String wifiPassword){
        this.wifiPassword =wifiPassword;
    }

    public void increaseViewCnt(){
        this.viewCnt += 1;
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

    public Map<String,Object> getBusinessInfo(){

        Map<String,Object> businessHoursInfoMap = new HashMap<>();
        businessHoursInfoMap.put("isOpen", null);
        businessHoursInfoMap.put("closed", null);
        businessHoursInfoMap.put("tmrOpen", null);

        //영업시간 데이터 없는 경우
        if(this.businessHours == null) {
            return businessHoursInfoMap;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalTime nowTime = now.toLocalTime();
        //현재 요일
        int dayOfWeekNumber = now.getDayOfWeek().getValue();

        boolean isOpen;
        //요일별
        switch(dayOfWeekNumber){
            case 7: //일
                //영업중 체크, 금일 영업 종료 시간
                if(this.businessHours.getOnSun() != null) {
                    isOpen = nowTime.isAfter(this.businessHours.getOnSun().getOpen()) && nowTime.isBefore(this.businessHours.getOnSun().getClosed());
                    businessHoursInfoMap.put("isOpen", isOpen);
                    businessHoursInfoMap.put("closed", this.businessHours.getOnSun().getClosed());
                }
                //내일 영업 시작 시간
                if(this.businessHours.getOnMon() != null){
                    businessHoursInfoMap.put("tmrOpen", this.businessHours.getOnMon().getOpen());
                }
                break ;
            case 1:
                //영업중 체크, 금일 영업 종료 시간
                if(this.businessHours.getOnMon() != null) {
                    isOpen = nowTime.isAfter(this.businessHours.getOnMon().getOpen()) && nowTime.isBefore(this.businessHours.getOnMon().getClosed());
                    businessHoursInfoMap.put("isOpen", isOpen);
                    businessHoursInfoMap.put("closed", this.businessHours.getOnMon().getClosed());
                }
                //내일 영업 시작 시간
                if(this.businessHours.getOnTue() != null){
                    businessHoursInfoMap.put("tmrOpen", this.businessHours.getOnTue().getOpen());
                }
                break ;
            case 2:
                //영업중 체크, 금일 영업 종료 시간
                if(this.businessHours.getOnTue() != null) {
                    isOpen = nowTime.isAfter(this.businessHours.getOnTue().getOpen()) && nowTime.isBefore(this.businessHours.getOnTue().getClosed());
                    businessHoursInfoMap.put("isOpen", isOpen);
                    businessHoursInfoMap.put("closed", this.businessHours.getOnTue().getClosed());
                }
                //내일 영업 시작 시간
                if(this.businessHours.getOnWed() != null){
                    businessHoursInfoMap.put("tmrOpen", this.businessHours.getOnWed().getOpen());
                }
                break ;
            case 3:
                //영업중 체크, 금일 영업 종료 시간
                if(this.businessHours.getOnWed() != null) {
                    isOpen = nowTime.isAfter(this.businessHours.getOnWed().getOpen()) && nowTime.isBefore(this.businessHours.getOnWed().getClosed());
                    businessHoursInfoMap.put("isOpen", isOpen);
                    businessHoursInfoMap.put("closed", this.businessHours.getOnWed().getClosed());
                }
                //내일 영업 시작 시간
                if(this.businessHours.getOnThu() != null){
                    businessHoursInfoMap.put("tmrOpen", this.businessHours.getOnThu().getOpen());
                }
                break ;
            case 4:
                //영업중 체크, 금일 영업 종료 시간
                if(this.businessHours.getOnThu() != null) {
                    isOpen = nowTime.isAfter(this.businessHours.getOnThu().getOpen()) && nowTime.isBefore(this.businessHours.getOnThu().getClosed());
                    businessHoursInfoMap.put("isOpen", isOpen);
                    businessHoursInfoMap.put("closed", this.businessHours.getOnThu().getClosed());
                }
                //내일 영업 시작 시간
                if(this.businessHours.getOnFri() != null){
                    businessHoursInfoMap.put("tmrOpen", this.businessHours.getOnFri().getOpen());
                }
                break ;
            case 5:
                //영업중 체크, 금일 영업 종료 시간
                if(this.businessHours.getOnFri() != null) {
                    isOpen = nowTime.isAfter(this.businessHours.getOnFri().getOpen()) && nowTime.isBefore(this.businessHours.getOnFri().getClosed());
                    businessHoursInfoMap.put("isOpen", isOpen);
                    businessHoursInfoMap.put("closed", this.businessHours.getOnFri().getClosed());
                }
                //내일 영업 시작 시간
                if(this.businessHours.getOnSat() != null){
                    businessHoursInfoMap.put("tmrOpen", this.businessHours.getOnSat().getOpen());
                }
                break ;
            case 6:
                //영업중 체크, 금일 영업 종료 시간
                if(this.businessHours.getOnSat() != null) {
                    isOpen = nowTime.isAfter(this.businessHours.getOnSat().getOpen()) && nowTime.isBefore(this.businessHours.getOnSat().getClosed());
                    businessHoursInfoMap.put("isOpen", isOpen);
                    businessHoursInfoMap.put("closed", this.businessHours.getOnSat().getClosed());
                }
                //내일 영업 시작 시간
                if(this.businessHours.getOnSun() != null){
                    businessHoursInfoMap.put("tmrOpen", this.businessHours.getOnSun().getOpen());
                }
                break ;
        }

        return businessHoursInfoMap;
    }
}
