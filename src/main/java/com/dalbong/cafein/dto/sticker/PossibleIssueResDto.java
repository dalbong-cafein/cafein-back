package com.dalbong.cafein.dto.sticker;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PossibleIssueResDto {

    private Boolean isPossibleIssue;

    private String reason;

}
