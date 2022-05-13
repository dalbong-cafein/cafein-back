package com.dalbong.cafein.dto.congestion;

import com.dalbong.cafein.domain.congestion.Congestion;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CongestionResDto {

    private Long writerId;

    private String nickname;

    private int congestionScore;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime regDateTime;

    public CongestionResDto(Congestion congestion){
        this.writerId = congestion.getMember().getMemberId();
        this.nickname = congestion.getMember().getNickname();
        this.congestionScore = congestion.getCongestionScore();
        this.regDateTime = congestion.getRegDateTime();
    }

}
