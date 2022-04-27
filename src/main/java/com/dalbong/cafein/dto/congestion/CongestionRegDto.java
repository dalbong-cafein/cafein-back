package com.dalbong.cafein.dto.congestion;

import com.dalbong.cafein.domain.congestion.Congestion;
import com.dalbong.cafein.domain.congestion.CongestionType;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.store.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CongestionRegDto {

    private Long storeId;

    private CongestionType type;

    public Congestion toEntity(Store store, Long principalId){
        return Congestion.builder()
                .store(store)
                .member(Member.builder().memberId(principalId).build())
                .type(type)
                .build();
    }

}
