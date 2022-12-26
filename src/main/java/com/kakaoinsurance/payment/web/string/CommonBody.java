package com.kakaoinsurance.payment.web.string;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommonBody {

    private String cardNumber;

    private String installment;

    private String expiryDate;

    private String cvc;

    private String price;

    private String vat;

    private String controlNumber;

    private String encryptCardInfo;

    private String spare;


    public void setCardNumber(String cardNumber){
        this.cardNumber = alignLeft(cardNumber, 20, " ");
    }

    public void setInstallment(String installment){
        this.installment = alignRight(installment, 2, "0");
    }

    public void setExpiryDate(String expiryDate){
        this.expiryDate = alignLeft(expiryDate, 4, " ");
    }

    public void setCvc(String cvc){
        this.cvc = alignLeft(cvc, 3, " ");
    }

    public void setPrice(String price){
        this.price = alignRight(price, 10, " ");
    }

    public void setVat(String vat){
        this.vat = alignRight(vat, 10, "0");
    }

    public void setControlNumber(String controlNumber){
        this.controlNumber = alignLeft(controlNumber, 20, " ");
    }

    public void setCardInfo(String cardInfo){
        this.encryptCardInfo = alignLeft(cardInfo, 300, " ");
    }

    public void setSpare(String spare){
        this.spare = alignLeft(spare, 47, " ");
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(cardNumber);
        stringBuilder.append(installment);
        stringBuilder.append(expiryDate);
        stringBuilder.append(cvc);
        stringBuilder.append(price);
        stringBuilder.append(vat);
        stringBuilder.append(controlNumber);
        stringBuilder.append(encryptCardInfo);
        stringBuilder.append(spare);

        return stringBuilder.toString();
    }

    // 우측정렬
    public String alignRight(String inputWord, int length, String blank) {
        if (inputWord.length() >= length) {
            return inputWord;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputWord.length()) {
            sb.append(blank);
        }
        sb.append(inputWord);

        return sb.toString();
    }

    // 좌측정렬
    public String alignLeft(String inputWord, int length, String blank) {
        if (inputWord.length() >= length) {
            return inputWord;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputWord.length()) {
            sb.append(blank);
        }
        sb.insert(0, inputWord);

        return sb.toString();
    }
}
