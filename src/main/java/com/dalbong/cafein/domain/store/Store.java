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
import com.dalbong.cafein.web.domain.recommend.Recommend;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"regMember","modMember","reviewList","storeImageList","heartList","nearStoreToSubwayStationArrayList","recommendList"})
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
    @JoinColumn(name = "business_hours_id")
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
        businessHoursInfoMap.put("open", null);
        businessHoursInfoMap.put("closed", null);
        businessHoursInfoMap.put("tmrOpen", null);

        //영업시간 데이터 없는 경우
        if(this.businessHours == null) {
            return businessHoursInfoMap;
        }

        //현재 일시
        LocalDateTime nowDateTime = LocalDateTime.now();

        //현재 요일
        int dayOfWeekNumber = nowDateTime.getDayOfWeek().getValue();

        boolean isOpen;

        //요일별
        switch(dayOfWeekNumber){
            case 7: //일

                //영업중 체크, 금일 영업 종료 시간
                if(this.businessHours.getOnSun() != null) {
                    LocalTime openTime = this.businessHours.getOnSun().getOpen();
                    LocalTime closedTime = this.businessHours.getOnSun().getClosed();

                    isOpen = checkIsOpen(nowDateTime, openTime, closedTime);
                    businessHoursInfoMap.put("isOpen", isOpen);
                    businessHoursInfoMap.put("open", openTime);
                    businessHoursInfoMap.put("closed", closedTime);
                }
                //내일 영업 시작 시간
                if(this.businessHours.getOnMon() != null){
                    businessHoursInfoMap.put("tmrOpen", this.businessHours.getOnMon().getOpen());
                }
                //전날 영업 종료 시간이 자정이 넘어갈 경우 - isOpen, closed 체크
                if(this.businessHours.getOnSat() != null){
                    checkBusinessAfterMidnight(businessHoursInfoMap, nowDateTime,
                            this.businessHours.getOnSat().getOpen(), this.businessHours.getOnSat().getClosed());
                }
                break ;

            case 1:
                //영업중 체크, 금일 영업 종료 시간
                if(this.businessHours.getOnMon() != null) {

                    LocalTime openTime = this.businessHours.getOnMon().getOpen();
                    LocalTime closedTime = this.businessHours.getOnMon().getClosed();

                    isOpen = checkIsOpen(nowDateTime, openTime, closedTime);
                    businessHoursInfoMap.put("isOpen", isOpen);
                    businessHoursInfoMap.put("open", openTime);
                    businessHoursInfoMap.put("closed", this.businessHours.getOnMon().getClosed());
                }
                //내일 영업 시작 시간
                if(this.businessHours.getOnTue() != null){
                    businessHoursInfoMap.put("tmrOpen", this.businessHours.getOnTue().getOpen());
                }
                //전날 영업 종료 시간이 자정이 넘어갈 경우 - isOpen, closed 체크
                if(this.businessHours.getOnSun() != null){
                    checkBusinessAfterMidnight(businessHoursInfoMap, nowDateTime,
                            this.businessHours.getOnSun().getOpen(), this.businessHours.getOnSun().getClosed());
                }
                break ;

            case 2:
                //영업중 체크, 금일 영업 종료 시간
                if(this.businessHours.getOnTue() != null) {

                    LocalTime openTime = this.businessHours.getOnTue().getOpen();
                    LocalTime closedTime = this.businessHours.getOnTue().getClosed();

                    isOpen = checkIsOpen(nowDateTime, openTime, closedTime);
                    businessHoursInfoMap.put("isOpen", isOpen);
                    businessHoursInfoMap.put("open", openTime);
                    businessHoursInfoMap.put("closed", closedTime);

                }
                //내일 영업 시작 시간
                if(this.businessHours.getOnWed() != null){
                    businessHoursInfoMap.put("tmrOpen", this.businessHours.getOnWed().getOpen());
                }
                //전날 영업 종료 시간이 자정이 넘어갈 경우 - isOpen, closed 체크
                if(this.businessHours.getOnMon() != null){
                    checkBusinessAfterMidnight(businessHoursInfoMap, nowDateTime,
                            this.businessHours.getOnMon().getOpen(), this.businessHours.getOnMon().getClosed());
                }
                break ;

            case 3:
                //영업중 체크, 금일 영업 종료 시간
                if(this.businessHours.getOnWed() != null) {

                    LocalTime openTime = this.businessHours.getOnWed().getOpen();
                    LocalTime closedTime = this.businessHours.getOnWed().getClosed();

                    isOpen = checkIsOpen(nowDateTime, openTime, closedTime);
                    businessHoursInfoMap.put("isOpen", isOpen);
                    businessHoursInfoMap.put("open", openTime);
                    businessHoursInfoMap.put("closed", this.businessHours.getOnWed().getClosed());
                }
                //내일 영업 시작 시간
                if(this.businessHours.getOnThu() != null){
                    businessHoursInfoMap.put("tmrOpen", this.businessHours.getOnThu().getOpen());
                }
                //전날 영업 종료 시간이 자정이 넘어갈 경우 - isOpen, closed 체크
                if(this.businessHours.getOnTue() != null){
                    checkBusinessAfterMidnight(businessHoursInfoMap, nowDateTime,
                            this.businessHours.getOnTue().getOpen(), this.businessHours.getOnTue().getClosed());
                }
                break ;

            case 4:
                //영업중 체크, 금일 영업 종료 시간
                if(this.businessHours.getOnThu() != null) {

                    LocalTime openTime = this.businessHours.getOnThu().getOpen();
                    LocalTime closedTime = this.businessHours.getOnThu().getClosed();

                    isOpen = checkIsOpen(nowDateTime, openTime, closedTime);
                    businessHoursInfoMap.put("isOpen", isOpen);
                    businessHoursInfoMap.put("open", openTime);
                    businessHoursInfoMap.put("closed", this.businessHours.getOnThu().getClosed());
                }
                //내일 영업 시작 시간
                if(this.businessHours.getOnFri() != null){
                    businessHoursInfoMap.put("tmrOpen", this.businessHours.getOnFri().getOpen());
                }
                //전날 영업 종료 시간이 자정이 넘어갈 경우 - isOpen, closed 체크
                if(this.businessHours.getOnWed() != null){
                    checkBusinessAfterMidnight(businessHoursInfoMap, nowDateTime,
                            this.businessHours.getOnWed().getOpen(), this.businessHours.getOnWed().getClosed());
                }
                break ;
            case 5:
                //영업중 체크, 금일 영업 종료 시간
                if(this.businessHours.getOnFri() != null) {

                    LocalTime openTime = this.businessHours.getOnFri().getOpen();
                    LocalTime closedTime = this.businessHours.getOnFri().getClosed();

                    isOpen = checkIsOpen(nowDateTime, openTime, closedTime);
                    businessHoursInfoMap.put("isOpen", isOpen);
                    businessHoursInfoMap.put("open", openTime);
                    businessHoursInfoMap.put("closed", this.businessHours.getOnFri().getClosed());
                }
                //내일 영업 시작 시간
                if(this.businessHours.getOnSat() != null){
                    businessHoursInfoMap.put("tmrOpen", this.businessHours.getOnSat().getOpen());
                }
                //전날 영업 종료 시간이 자정이 넘어갈 경우 - isOpen, closed 체크
                if(this.businessHours.getOnThu() != null){
                    checkBusinessAfterMidnight(businessHoursInfoMap, nowDateTime,
                            this.businessHours.getOnThu().getOpen(), this.businessHours.getOnThu().getClosed());
                    }
                break ;

            case 6:
                //영업중 체크, 금일 영업 종료 시간
                if(this.businessHours.getOnSat() != null) {

                    LocalTime openTime = this.businessHours.getOnSat().getOpen();
                    LocalTime closedTime = this.businessHours.getOnSat().getClosed();

                    isOpen = checkIsOpen(nowDateTime, openTime, closedTime);
                    businessHoursInfoMap.put("isOpen", isOpen);
                    businessHoursInfoMap.put("open", openTime);
                    businessHoursInfoMap.put("closed", this.businessHours.getOnSat().getClosed());
                }
                //내일 영업 시작 시간
                if(this.businessHours.getOnSun() != null){
                    businessHoursInfoMap.put("tmrOpen", this.businessHours.getOnSun().getOpen());
                }
                //전날 영업 종료 시간이 자정이 넘어갈 경우 - isOpen, closed 체크
                if(this.businessHours.getOnFri() != null){
                    checkBusinessAfterMidnight(businessHoursInfoMap, nowDateTime,
                            this.businessHours.getOnFri().getOpen(), this.businessHours.getOnFri().getClosed());
                }
                break ;
        }

        return businessHoursInfoMap;
    }

    private void checkBusinessAfterMidnight(Map<String, Object> businessHoursInfoMap, LocalDateTime nowDateTime,
                                            LocalTime openTimeDayBefore, LocalTime closedTimeDayBefore) {

        if(openTimeDayBefore.isAfter(closedTimeDayBefore) && closedTimeDayBefore.isAfter(nowDateTime.toLocalTime())){
            businessHoursInfoMap.put("isOpen", true);
            businessHoursInfoMap.put("closed", closedTimeDayBefore);
        }

    }

    private boolean checkIsOpen(LocalDateTime nowDateTime, LocalTime openTime, LocalTime closedTime) {

        LocalDateTime openDateTime = LocalDate.now().atTime(openTime);
        LocalDateTime closedDateTime = LocalDate.now().atTime(closedTime);

        //영업 종료 시간이 다음 날짜로 넘어갈 경우
        if(openDateTime.isAfter(closedDateTime) || openDateTime.isEqual(closedDateTime)){
            closedDateTime = closedDateTime.plusHours(24);
        }
        return nowDateTime.isAfter(openDateTime) && nowDateTime.isBefore(closedDateTime);
    }
}
