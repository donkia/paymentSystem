package com.kakaoinsurance.payment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CardInfo implements Serializable {

    private String cardNumber;

    private String expiryDate;

    private String cvc;

    @Override
    public String toString() {
        return cardNumber + '|' + expiryDate + '|' + cvc;
    }
}
