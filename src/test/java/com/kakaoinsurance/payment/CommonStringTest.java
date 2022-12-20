package com.kakaoinsurance.payment;
import com.kakaoinsurance.payment.web.string.CommonBody;
import com.kakaoinsurance.payment.web.string.CommonHeader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class CommonStringTest {

    @Test
    void 우측정렬_빈칸공백채우기(){
        String inputWord = "446";
        int length = 4;
        String blank = " ";

        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputWord.length()) {
            sb.append(blank);
        }
        sb.append(inputWord);

        Assertions.assertEquals(" 446", sb.toString());
    }

    @Test
    void 우측정렬_빈칸0채우기(){
        String inputWord = "446";
        int length = 4;
        String blank = "0";

        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputWord.length()) {
            sb.append(blank);
        }
        sb.append(inputWord);

        Assertions.assertEquals("0446", sb.toString());
    }

    @Test
    void 좌측정렬_빈칸공백채우기(){
        String inputWord = "446";
        int length = 4;
        String blank = " ";

        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputWord.length()) {
            sb.append(blank);
        }
        sb.insert(0, inputWord);

        Assertions.assertEquals("446 ", sb.toString());
    }

    @Test
    void 좌측정렬_빈칸0채우기(){
        String inputWord = "446";
        int length = 4;
        String blank = "0";

        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputWord.length()) {
            sb.append(blank);
        }
        sb.insert(0, inputWord);
        Assertions.assertEquals("4460", sb.toString());
    }

    @Test
    void 헤더만들기(){

        CommonHeader header = new CommonHeader();
        header.setDataLength(446);
        header.setCategory("PAYMENT");
        header.setControlNumber("XXXXXXXXXXXXXXXXXXXX");

        Assertions.assertEquals(34, header.toString().length());
        Assertions.assertEquals(" 446PAYMENT   XXXXXXXXXXXXXXXXXXXX", header.toString());
    }

    @Test
    void 바디만들기(){
        CommonBody body = new CommonBody();
        body.setCardNumber("1234567890123456");
        body.setInstallment("0");
        body.setExpiryDate("1125");
        body.setCvc("777");
        body.setPrice("110000");
        body.setVat("10000");
        body.setControlNumber("");
        body.setCardInfo("YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY");
        body.setSpare("");

        System.out.println(body.toString());

        Assertions.assertEquals(416, body.toString().length());
    }

}
