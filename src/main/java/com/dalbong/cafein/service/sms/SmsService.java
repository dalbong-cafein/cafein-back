package com.dalbong.cafein.service.sms;

public interface SmsService {

    String createCertifyNum();

    void sendSms(String toNumber, String certifyNum);
}
