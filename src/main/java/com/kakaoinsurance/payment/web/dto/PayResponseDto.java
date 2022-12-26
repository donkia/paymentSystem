package com.kakaoinsurance.payment.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PayResponseDto {

    private String controlNumber;   //관리번호

    private String stringData;      //카드사에 전달한 string data

    @Builder
    public PayResponseDto(String controlNumber, String stringData){
        this.controlNumber = controlNumber;
        this.stringData = stringData;
    }
}
