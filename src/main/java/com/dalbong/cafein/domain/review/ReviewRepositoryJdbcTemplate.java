package com.dalbong.cafein.domain.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewRepositoryJdbcTemplate {

    List<ReviewMappedDto> getMultiEntity(Long storeId);

}
