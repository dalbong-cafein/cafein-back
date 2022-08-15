package com.dalbong.cafein.dataSet.util.excel;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ExcelFileType {

    /**
     *
     * 엑셀파일을 읽어서 Workbook 객체에 리턴한다.
     * XLS와 XLSX 확장자를 비교한다.
     *
     */
    public static Workbook getWorkbook(MultipartFile excelFile) throws IOException {

        String extension = FilenameUtils.getExtension(excelFile.getOriginalFilename());

        if (!extension.equals("xlsx") && !extension.equals("xls")) {
            throw new IOException("엑셀파일만 업로드 해주세요.");
        }
        Workbook wb = null;

        /*
         * 파일의 확장자를 체크해서 .XLS 라면 HSSFWorkbook에
         * .XLSX라면 XSSFWorkbook에 각각 초기화 한다.
         */
        if (extension.equals("xlsx")) {
            wb = new XSSFWorkbook(excelFile.getInputStream());
        } else if (extension.equals("xls")) {
            wb = new HSSFWorkbook(excelFile.getInputStream());
        }

        return wb;

    }
}


