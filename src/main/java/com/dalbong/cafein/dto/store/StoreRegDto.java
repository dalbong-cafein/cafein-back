package com.dalbong.cafein.dto.store;

import com.dalbong.cafein.domain.address.Address;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.store.Feature;
import com.dalbong.cafein.domain.store.SocketCnt;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.dto.businessHours.BusinessHoursRegDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StoreRegDto {

    private String storeName;

    private String siNm; //시도명
    private String sggNm; //시군구
    private String rn; //도로명

    private BusinessHoursRegDto businessHoursRegDto;

    private int americano;

    private String phone;

    private String website;

    private int mapX;

    private int mapY;

    private SocketCnt socketCnt;

    @Builder.Default //테스트 코드 용도
    private Set<String> hashTagSet = new HashSet<>();

    @Builder.Default //테스트 코드 용도
    private List<Feature> featureList = new ArrayList<>();

    //TODO image

    public Store toEntity(Long principalId){

        Address address = new Address(siNm, sggNm, rn);

        return Store.builder()
                .member(Member.builder().memberId(principalId).build())
                .storeName(storeName)
                .address(address)
                .americano(americano)
                .mapX(mapX).mapY(mapY)
                .featureList(featureList)
                .hashTagSet(hashTagSet)
                .phone(phone)
                .socketCnt(socketCnt)
                .website(website)
                .build();
    }

}
