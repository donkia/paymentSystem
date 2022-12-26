package com.kakaoinsurance.payment.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorDto {

    private int status;
    private String message;
}
