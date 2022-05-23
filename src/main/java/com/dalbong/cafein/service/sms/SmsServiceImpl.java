package com.dalbong.cafein.service.sms;

import lombok.extern.slf4j.Slf4j;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Random;

@Slf4j
@Transactional
@Service
public class SmsServiceImpl implements SmsService{

    @Value("${coolsms.apiKey}")
    private String apiKey;

    @Value("${coolsms.apiSecret}")
    private String apiSecret;

    @Value("${coolsms.fromNumber}")
    private String fromNumber;


    /**
     * 6자리 인증번호 생성
     */
    @Transactional
    @Override
    public String createCertifyNum() {

        //4자리 인증번호 생성
        Random rand  = new Random();
        String certifyNumber = "";
        for(int i=0; i<6; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            certifyNumber+=ran;
        }

        return certifyNumber;
    }

    /**
     * sms 문자메세지 전송
     */
    @Transactional
    @Override
    public void sendSms(String toNumber, String certifyNum) {

        Message coolsms = new Message(apiKey, apiSecret);


        HashMap<String, String> params = new HashMap<>();

        params.put("to", toNumber);
        params.put("from", fromNumber);
        params.put("type", "SMS");
        params.put("text", "[Cafein] 인증번호 " + certifyNum + "을 입력해주세요.");
        params.put("app_version", "test app 1.2");

        try{
            JSONObject object = (JSONObject) coolsms.send(params);
            System.out.println(object.toString());

        }catch(CoolsmsException e){
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }

    }
}
