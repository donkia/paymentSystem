package com.kakaoinsurance.payment.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
//@AllArgsConstructor
@ToString
public class PaymentListResponseDto {

    private String controlNumber; // 관리번호

    private String cardNumber;  // 카드번호

    private String expiryDate;  // 유효기간

    private String cvc; // cvc

    private String status;  // 결제상태(결제, 취소)

    private int price;  // 결제 또는 취소 금액

    private int vat;    // 부가가치세


    @Builder
    PaymentListResponseDto(String controlNumber, String cardNumber, String expiryDate, String cvc, String status, int price, int vat) {
        this.controlNumber = controlNumber;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvc = cvc;
        this.status = status;
        this.price = price;
        this.vat = vat;
    }

}
