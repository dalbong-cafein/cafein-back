package com.dalbong.cafein.dataSet.review;

import com.dalbong.cafein.dataSet.util.excel.ExcelRead;
import com.dalbong.cafein.domain.review.ReviewRepository;
import com.dalbong.cafein.domain.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ExcelReviewDataService {

    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;
    private final ExcelRead excelRead;

    @Transactional
    public void register(MultipartFile excelFile) throws IOException {

        //파일 읽기
        List<ExcelReviewDataDto> excelReviewDataDtoList = excelRead.read(excelFile);

        for(ExcelReviewDataDto dto : excelReviewDataDtoList){
            System.out.println(dto);
        }
        //데이터 매핑

        //리뷰 데이터 저장

    }


}
