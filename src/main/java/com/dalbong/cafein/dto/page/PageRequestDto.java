package com.dalbong.cafein.dto.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDto {

    private int page;

    private int size;

    private String keyword;

    public PageRequestDto(){
        this.page = 1;
        this.size = 15;
    }

    public Pageable getPageable(){
        return PageRequest.of(this.page-1, this.size);
    }

    public Pageable getPageable(Sort sort){
        return PageRequest.of(this.page-1, this.size, sort);
    }


}
