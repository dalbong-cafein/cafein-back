package com.dalbong.cafein.dataSet.store;

import com.dalbong.cafein.dto.businessHours.BusinessHoursUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class JsonStoreDataDto {

    private String storeName;

    private String phone;

    private BusinessHoursUpdateDto businessHours;

}
