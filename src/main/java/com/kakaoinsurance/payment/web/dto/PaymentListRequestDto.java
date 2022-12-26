package com.kakaoinsurance.payment.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class PaymentListRequestDto {

    private String controlNumber;   //  관리번호
}
