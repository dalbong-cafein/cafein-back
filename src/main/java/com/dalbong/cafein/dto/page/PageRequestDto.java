package com.dalbong.cafein.dto.page;

import com.dalbong.cafein.domain.member.MemberState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@AllArgsConstructor
@Data
public class PageRequestDto {

    private int page;

    private int size;

    private String keyword;

    //회원Id: m, 회원명: mn, 핸드폰: p, 가게Id: s, 가게명: sn, 주소: a, 내용: c, 쿠폰Id : cp, 리뷰Id : r, 신고Id : rp
    private String[] searchType;

    private String[] sggNms;

    private MemberState[] memberStates;

    private Boolean isOnlyImage;

    private String sort = "DESC";

    public PageRequestDto(){
        this.page = 1;
        this.size = 9;
        this.sort = "DESC";
    }

    public Pageable getPageable(){
        return PageRequest.of(this.page-1, this.size);
    }

    public Pageable getPageable(Sort sort){
        return PageRequest.of(this.page-1, this.size, sort);
    }




}
