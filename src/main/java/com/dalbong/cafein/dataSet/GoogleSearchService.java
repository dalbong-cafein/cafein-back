package com.dalbong.cafein.dataSet;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class GoogleSearchService {


    public void placeSearch(Map<String,Object> searchData){

        List<Map<String,Object>> searchPlaceData =  (List<Map<String,Object>>) searchData.get("results");



    }

}
