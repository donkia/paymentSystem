package com.kakaoinsurance.payment.web.string;

import lombok.Getter;

@Getter
public class CommonHeader {

    private String dataLength;

    private String category;

    private String controlNumber;

    public void setDataLength(int dataLength) {
        this.dataLength = alignRight(Integer.toString(dataLength), 4, " ");
    }

    public void setCategory(String category) {
        this.category = alignLeft(category, 10, " ");
    }

    public void setControlNumber(String controlNumber) {
        this.controlNumber = alignLeft(controlNumber, 20, " ");
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(dataLength);
        stringBuilder.append(category);
        stringBuilder.append(controlNumber);
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
