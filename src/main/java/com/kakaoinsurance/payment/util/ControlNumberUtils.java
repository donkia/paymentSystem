package com.kakaoinsurance.payment.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ControlNumberUtils {

    public static String createControlNumber(){

        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
    }
}
