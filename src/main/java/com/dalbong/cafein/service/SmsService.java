package com.dalbong.cafein.service;

public interface SmsService {

    String createCertifyNum();

    void sendSms(String toNumber, String certifyNum);
}
