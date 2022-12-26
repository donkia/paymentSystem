package com.kakaoinsurance.payment.util;

import com.kakaoinsurance.payment.domain.CardInfo;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class AES256UtilsTest {

    private String iv;
    private Key keySpec;

    static String key = "kakao-insurance.";

    @BeforeEach
    public void setting() throws UnsupportedEncodingException {
        this.iv = key.substring(0, 16);
        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes("UTF-8");
        int len = b.length;
        if (len > keyBytes.length) {
            len = keyBytes.length;
        }
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

        this.keySpec = keySpec;
    }


    public String encrypt(String str) throws NoSuchAlgorithmException,
            GeneralSecurityException, UnsupportedEncodingException {
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
        byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
        String enStr = new String(Base64.encodeBase64(encrypted));
        return enStr;
    }

    public String decrypt(String str) throws NoSuchAlgorithmException,
            GeneralSecurityException, UnsupportedEncodingException {
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
        byte[] byteStr = Base64.decodeBase64(str.getBytes());
        return new String(c.doFinal(byteStr), "UTF-8");
    }

    public CardInfo createCardInfo(){
        String cardNumber = "12345678901234567890";
        String expiryDate = "0122";
        String cvc = "111";

        CardInfo cardInfo = new CardInfo(cardNumber, expiryDate, cvc);
        return cardInfo;
    }

    @Test
    void 카드정보_암호화_복호화() throws GeneralSecurityException, UnsupportedEncodingException {

        CardInfo cardInfo = createCardInfo();

        String encryptCardInfo = encrypt(cardInfo.toString());
        System.out.println(encryptCardInfo);

        Assertions.assertEquals(cardInfo.toString(), decrypt(encryptCardInfo));

    }

    @Test
    void 카브정보_복호화후_일치여부확인() throws GeneralSecurityException, UnsupportedEncodingException {
        CardInfo cardInfo = createCardInfo();

        String encryptCardInfo = encrypt(cardInfo.toString());
        String decryptCardInfo = decrypt(encryptCardInfo);

        Assertions.assertEquals("12345678901234567890", decryptCardInfo.split("\\|")[0]);

    }


}