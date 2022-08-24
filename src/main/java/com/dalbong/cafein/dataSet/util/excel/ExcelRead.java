package com.dalbong.cafein.dataSet.util.excel;

import com.dalbong.cafein.dataSet.review.ExcelReviewDataDto;
import com.dalbong.cafein.domain.review.Recommendation;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ExcelRead {

    public List<ExcelReviewDataDto> readReviewData(MultipartFile excelFile) throws IOException {

        //엑셀파일을 읽어 들인다.
        Workbook wb = ExcelFileType.getWorkbook(excelFile);

        //엑셀 파일에서 첫번째 시트를 가지고 온다.
        Sheet sheet = wb.getSheetAt(0);

        System.out.println("Sheet 이름: "+ wb.getSheetName(0));
        System.out.println("데이터가 있는 Sheet의 수 :" + wb.getNumberOfSheets());


        //sheet에서 유효한(데이터가 있는) 행의 개수를 가져온다.
        int numOfRows = sheet.getPhysicalNumberOfRows();

        List<ExcelReviewDataDto> excelReviewDataDtoList = new ArrayList<>();

        System.out.println(numOfRows);

        /**
         * 각 Row만큼 반복을 한다.
         */
        for(int rowIndex = 2; rowIndex < numOfRows; rowIndex++) {

            /*
             * 워크북에서 가져온 시트에서 rowIndex에 해당하는 Row를 가져온다.
             * 하나의 Row는 여러개의 Cell을 가진다.
             */
            Row row = sheet.getRow(rowIndex);

            String storeName = row.getCell(0).getStringCellValue();
            int socket = (int) row.getCell(1).getNumericCellValue();
            int wifi = (int) row.getCell(2).getNumericCellValue();
            int restroom = (int) row.getCell(3).getNumericCellValue();
            int tableSize = (int) row.getCell(4).getNumericCellValue();
            Recommendation recommendation = Recommendation.valueOf(row.getCell(6).getStringCellValue());

            ExcelReviewDataDto excelReviewDataDto = new ExcelReviewDataDto(storeName, socket, wifi, restroom, tableSize, recommendation);

            System.out.println(excelReviewDataDto);
            excelReviewDataDtoList.add(excelReviewDataDto);

        }

        return excelReviewDataDtoList;
    }
}
