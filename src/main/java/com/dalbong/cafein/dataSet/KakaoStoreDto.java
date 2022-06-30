package com.dalbong.cafein.dataSet;

import com.dalbong.cafein.domain.address.Address;
import lombok.Data;

import javax.persistence.Embedded;
import java.util.Arrays;
import java.util.Map;

@Data
public class KakaoStoreDto {

    public KakaoStoreDto(Map<String,Object> attributes){

        this.storeName = (String) attributes.get("place_name");
        this.phone = (String) attributes.get("phone");
        String roadAddress = (String) attributes.get("road_address_name");

        String[] roadAddressArray = roadAddress.split(" ",4);

        System.out.println(Arrays.toString(roadAddressArray));

        if(roadAddress != null && !roadAddress.equals("")){

            if(roadAddressArray.length < 4){
                this.address = null;
            }else{
                this.address = new Address(
                        roadAddressArray[0],roadAddressArray[1], roadAddressArray[2],
                        roadAddressArray[3],null);
            }
        }

        String stringX = (String) attributes.get("x");
        String stringY = (String) attributes.get("y");

        this.x =Double.parseDouble(stringX);
        this.y =Double.parseDouble(stringY);

        this.kakaoId = (String) attributes.get("id");
    }

    private String storeName;

    private String phone;

    @Embedded
    private Address address;

    private Double x;

    private Double y;

    private String kakaoId;

}
