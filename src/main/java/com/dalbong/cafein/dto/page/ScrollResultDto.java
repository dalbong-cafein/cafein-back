package com.dalbong.cafein.dto.page;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class ScrollResultDto<DTO, EN> {

    private List<DTO> dtoList;

    private int totalPage;

    private int page;

    private int size;

    private boolean last;


    public ScrollResultDto(Page<EN> result, Function<EN,DTO> fn ){

        dtoList = result.stream().map(fn).collect(Collectors.toList());
        totalPage = result.getTotalPages();
        last = result.isLast();
        makePageList(result.getPageable());
    }


    private void makePageList(Pageable pageable){
        this.page = pageable.getPageNumber() + 1; // 0부터 시작하므로 1을 추가
        this.size = pageable.getPageSize();
    }


}
