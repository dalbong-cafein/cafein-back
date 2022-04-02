package com.dalbong.cafein.dataSet;

import com.dalbong.cafein.domain.address.Address;
import lombok.Data;

import javax.persistence.Embedded;
import java.util.Map;

@Data
public class NaverStoreDto {

    public NaverStoreDto(Map<String,Object> attributes){

        this.storeName = (String) attributes.get("title");
        this.link = (String) attributes.get("link");
        this.telephone = (String) attributes.get("telephone");
        String mapXString = (String) attributes.get("mapx");
        this.mapX = Integer.parseInt(mapXString);

        String mapYString = (String) attributes.get("mapy");
        this.mapY = Integer.parseInt(mapYString);

        String roadAddress = (String) attributes.get("roadAddress");

        String[] roadAddressArray = roadAddress.split(" ",4);

        String detail = "";
        try{
            detail = roadAddressArray[4];
        }catch (ArrayIndexOutOfBoundsException e){
            e.getMessage();
        }

        this.address = new Address(
                roadAddressArray[0],roadAddressArray[1], roadAddressArray[2],
                roadAddressArray[3], detail);

    }

    private String storeName;

    private String link = "";

    private String telephone = "";

    @Embedded
    private Address address;

    private Integer mapX;

    private Integer mapY;
}
