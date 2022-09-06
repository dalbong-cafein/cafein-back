package com.dalbong.cafein.dataSet.util.json;

import com.dalbong.cafein.domain.store.Store;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Component
public class JsonUtil {

    public void write(List<Store> storeList, String sggNm){

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
            FileWriter file = new FileWriter("c:/cafein_crawling/store_list_"+sggNm+".json");
            file.write(totalJsonObject.toJSONString());
            file.flush();
            file.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
