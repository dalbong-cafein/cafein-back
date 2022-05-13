package com.dalbong.cafein.dto.congestion;

import com.dalbong.cafein.domain.congestion.Congestion;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.store.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CongestionRegDto {

    private Long storeId;

    @Max(value = 3) @Min(value = 1)
    private int congestionScore;

    public Congestion toEntity(Store store, Long principalId){
        return Congestion.builder()
                .store(store)
                .member(Member.builder().memberId(principalId).build())
                .congestionScore(congestionScore)
                .build();
    }

}
