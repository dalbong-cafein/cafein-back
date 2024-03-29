package com.dalbong.cafein.dataSet.util.json;

import com.dalbong.cafein.domain.store.Store;
import edu.emory.mathcs.backport.java.util.Arrays;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

@Component
public class JsonUtil {

    public void write(List<Store> storeList){

        JSONObject totalJsonObject = new JSONObject();

        totalJsonObject.put("totalCnt", storeList.size());

        JSONArray jsonArray = new JSONArray();

        for(Store store : storeList){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("storeName", store.getStoreName());
            jsonObject.put("sggNm", store.getAddress().getSggNm());
            jsonObject.put("phone", store.getPhone());

            jsonArray.add(jsonObject);
        }

        totalJsonObject.put("storeList", jsonArray);

        //json 파일 생성
        try {
            FileWriter file = new FileWriter("c:/cafein_crawling/store_list_.json");
            file.write(totalJsonObject.toJSONString());
            file.flush();
            file.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public Object read(String path) throws IOException, ParseException {

        JSONParser parser = new JSONParser();
        // JSON 파일 읽기
        Reader reader = new FileReader(path);

        return parser.parse(reader);
    }
}
