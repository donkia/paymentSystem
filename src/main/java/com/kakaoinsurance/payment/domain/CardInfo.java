package com.kakaoinsurance.payment.domain;

import lombok.Getter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
public class CardInfo implements Serializable {

    private String cardNumber;

    private String expiryDate;

    private String cvc;
}
