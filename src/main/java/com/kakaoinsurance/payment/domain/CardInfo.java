package com.kakaoinsurance.payment.domain;

import com.kakaoinsurance.payment.util.AES256Utils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

//@Embeddable
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

    public CardInfo(String decryptCardInfo) throws GeneralSecurityException, UnsupportedEncodingException {
        String[] decryptData = decryptCardInfo.split("\\|");
        this.cardNumber = decryptData[0];
        this.expiryDate = decryptData[1];
        this.cvc = decryptData[2];
    }
}
