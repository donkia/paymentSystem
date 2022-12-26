package com.kakaoinsurance.payment.util;

import com.kakaoinsurance.payment.domain.CardInfo;
import com.kakaoinsurance.payment.domain.Payment;
import com.kakaoinsurance.payment.web.dto.CancelRequestDto;
import com.kakaoinsurance.payment.web.dto.PayRequestDto;
import com.kakaoinsurance.payment.web.string.CommonBody;
import com.kakaoinsurance.payment.web.string.CommonHeader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

@Slf4j
public final class StringDataUtils {

    private static AES256Utils aes256Utils;

    public static String setPayStringData(PayRequestDto dto, String controlNumber) throws UnsupportedEncodingException, GeneralSecurityException {

        if(aes256Utils == null){
            aes256Utils = new AES256Utils();
        }
        CommonBody body = new CommonBody();
        body.setSpare("");
        body.setVat(Integer.toString(dto.getVat().get()));
        body.setCardInfo(aes256Utils.encrypt(dto.getCardNumber() + "|" + dto.getExpiryDate() + "|" + dto.getCvc()));
        body.setCvc(dto.getCvc());
        body.setPrice(Integer.toString(dto.getPrice()));
        body.setExpiryDate(dto.getExpiryDate());
        body.setInstallment(Integer.toString(dto.getInstallment()));
        body.setCardNumber(dto.getCardNumber());
        body.setControlNumber("");

        CommonHeader header = new CommonHeader();
        header.setCategory("PAYMENT");
        header.setControlNumber(controlNumber);
        header.setDataLength(body.toString().length()+header.getCategory().length() + header.getControlNumber().length());

        return header.toString() + body.toString();


    }

    public static String setCancelStringData(CancelRequestDto dto, Payment payment, String cancelControlNumber) throws UnsupportedEncodingException, GeneralSecurityException {

        CardInfo cardInfo = new CardInfo(AES256Utils.decrypt(payment.getEncryptCardInfo()));

        CommonBody body = new CommonBody();
        body.setSpare("");
        body.setVat(Integer.toString(dto.getVat().get()));
        body.setCardInfo(payment.getEncryptCardInfo());
        body.setCvc(cardInfo.getCvc());
        body.setPrice(Integer.toString(dto.getCancelPrice()));
        body.setExpiryDate(cardInfo.getExpiryDate());
        body.setInstallment("0");
        body.setCardNumber(cardInfo.getCardNumber());
        body.setControlNumber(payment.getControlNumber());

        CommonHeader header = new CommonHeader();
        header.setCategory("CANCEL");
        header.setControlNumber(cancelControlNumber);
        header.setDataLength(body.toString().length()+header.getCategory().length() + header.getControlNumber().length());


        log.info("cancel : {}",header.toString() + body.toString());

        return header.toString() + body.toString();


    }
}
