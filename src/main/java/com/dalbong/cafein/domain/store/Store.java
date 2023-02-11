package com.dalbong.cafein.domain.store;

import com.dalbong.cafein.domain.BaseEntity;
import com.dalbong.cafein.domain.address.Address;
import com.dalbong.cafein.domain.businessHours.BusinessHours;
import com.dalbong.cafein.domain.businessHours.Day;
import com.dalbong.cafein.domain.heart.Heart;
import com.dalbong.cafein.domain.image.StoreImage;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.nearStoreToSubwayStation.NearStoreToSubwayStation;
import com.dalbong.cafein.domain.nearStoreToUniversity.NearStoreToUniversity;
import com.dalbong.cafein.domain.review.Recommendation;
import com.dalbong.cafein.domain.review.Review;
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
@ToString(exclude = {"regMember","modMember","reviewList","storeImageList","heartList",
        "nearStoreToSubwayStationList","nearStoreToUniversity","recommendList"})
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
    private LocalDateTime infoModDateTime = LocalDateTime.now();

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
    private List<NearStoreToSubwayStation> nearStoreToSubwayStationList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "store",fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<NearStoreToUniversity> nearStoreToUniversityList = new ArrayList<>();

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
        businessHoursInfoMap.put("holidayType", null);
        businessHoursInfoMap.put("closed", null);
        businessHoursInfoMap.put("nextOpen", null);

        //영업시간 데이터 없는 경우
        if(this.businessHours == null) {
            return businessHoursInfoMap;
        }

        //현재 일시
        LocalDateTime nowDateTime = LocalDateTime.now();

        //현재 요일
        int dayOfWeekNumber = nowDateTime.getDayOfWeek().getValue();

        //요일별
        switch(dayOfWeekNumber){
            case 7: //일
                getBusinessHoursInfoMap(this.businessHours.getOnSun(), this.businessHours.getOnSat(),
                        this.businessHours.getOnMon(), nowDateTime, businessHoursInfoMap);
                break ;

            case 1: //월
                getBusinessHoursInfoMap(this.businessHours.getOnMon(), this.businessHours.getOnSun(),
                        this.businessHours.getOnTue(), nowDateTime, businessHoursInfoMap);
                break ;

            case 2: //화
                getBusinessHoursInfoMap(this.businessHours.getOnTue(), this.businessHours.getOnMon(),
                        this.businessHours.getOnWed(), nowDateTime, businessHoursInfoMap);
                break ;

            case 3: //수
                getBusinessHoursInfoMap(this.businessHours.getOnWed(), this.businessHours.getOnTue(),
                        this.businessHours.getOnThu(), nowDateTime, businessHoursInfoMap);
                break ;

            case 4: //목
                getBusinessHoursInfoMap(this.businessHours.getOnThu(), this.businessHours.getOnWed(),
                        this.businessHours.getOnFri(), nowDateTime, businessHoursInfoMap);
                break ;
            case 5: //금
                getBusinessHoursInfoMap(this.businessHours.getOnFri(), this.businessHours.getOnTue(),
                        this.businessHours.getOnSat(), nowDateTime, businessHoursInfoMap);
                break ;

            case 6: //토
                getBusinessHoursInfoMap(this.businessHours.getOnSat(), this.businessHours.getOnFri(),
                        this.businessHours.getOnSun(), nowDateTime, businessHoursInfoMap);
                break ;
        }

        return businessHoursInfoMap;
    }

    private void getBusinessHoursInfoMap(Day today, Day yesterday, Day tomorrow, LocalDateTime nowDateTime,
                                                       Map<String, Object> businessHoursInfoMap) {

        LocalTime openTime = null;
        LocalTime closedTime = null;
        LocalTime tmrOpenTime = null;

        //영업중 체크, 금일 영업 종료 시간
        if(today != null) {
            boolean isOpen;

            //휴무일 경우
            if(today.getHolidayType() != null){
                isOpen = false;
            }
            //영업 날일 경우
            else{
                openTime = today.getOpen();
                closedTime = today.getClosed();
                isOpen = checkIsOpen(nowDateTime, openTime, closedTime);
            }

            businessHoursInfoMap.put("isOpen", isOpen);
            businessHoursInfoMap.put("holidayType", today.getHolidayType());
            businessHoursInfoMap.put("closed", closedTime);
        }

        //내일 영업 시작 시간
        if(tomorrow != null){
            tmrOpenTime = tomorrow.getOpen();
        }

        //다음 오픈 시간
        LocalTime nextOpenTime = getNextOpenTime(nowDateTime, openTime, tmrOpenTime);
        businessHoursInfoMap.put("nextOpen", nextOpenTime);

        //전날 영업 종료 시간이 자정이 넘어갈 경우 - isOpen, closed 체크
        if(yesterday != null && yesterday.getHolidayType() == null){
            checkBusinessAfterMidnight(businessHoursInfoMap, nowDateTime, yesterday);
        }

    }

    private LocalTime getNextOpenTime(LocalDateTime nowDateTime, LocalTime openTime, LocalTime tmrOpenTime) {

            //금일 오픈 시간 데이터가 없을 경우 or 금일 오픈 시간 < 현재 시간일 경우
            if(openTime == null || nowDateTime.toLocalTime().isAfter(openTime)){
                return  tmrOpenTime;
            }
            //금일 오픈 시간 >= 현재 시간일 경우
            else{
                return openTime;
        }
    }


    private void checkBusinessAfterMidnight(Map<String, Object> businessHoursInfoMap, LocalDateTime nowDateTime, Day yesterday) {

        //어제 오픈시간, 종료시간
        LocalTime openTimeYesterday = yesterday.getOpen();
        LocalTime closedTimeYesterday = yesterday.getClosed();

        if(openTimeYesterday.isAfter(closedTimeYesterday) && closedTimeYesterday.isAfter(nowDateTime.toLocalTime())){
            businessHoursInfoMap.put("isOpen", true);
            businessHoursInfoMap.put("closed", closedTimeYesterday);
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
