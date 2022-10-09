package com.dalbong.cafein.dto.admin.congestion;

import com.dalbong.cafein.domain.congestion.Congestion;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminCongestionResDto {

    private Long writerId;

    private String nicknameOfWriter;

    private int congestionScore;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime regDateTime;

    public AdminCongestionResDto(Congestion congestion){
        this.writerId = congestion.getMember().getMemberId();
        this.nicknameOfWriter = congestion.getMember().getNickname();
        this.congestionScore = congestion.getCongestionScore();
        this.regDateTime = congestion.getRegDateTime();
    }


}
