package com.kakaoinsurance.payment.domain;

import com.kakaoinsurance.payment.util.AES256Utils;
import com.kakaoinsurance.payment.web.dto.PayRequestDto;
import com.kakaoinsurance.payment.web.dto.PayResponseDto;
import com.kakaoinsurance.payment.web.dto.PaymentListResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@ToString

public class Payment extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 20)
    private String controlNumber; //관리번호

    @Column(length = 20)
    private String cancelControlNumber; // 결제 취소 관리번호

    private int price; //가격

    @Enumerated(EnumType.STRING)
    @Column(length = 15)
    private PaymentStatus status;  //상태

    private int vat; //부가가치세

    @Column(length = 2)
    private int installment; //할부개월수

    //@Embedded
    //private CardInfo cardInfo; //카드정보

    private String encryptCardInfo; //암호화된 카드정보

    @Column(length = 450)
    private String stringData; // string 데이터


    @Builder
    public Payment(String controlNumber, int price, int vat, int installment, String cardNumber, String expiryDate, String cvc, String stringData) throws GeneralSecurityException, UnsupportedEncodingException {
        AES256Utils aes256Utils = new AES256Utils();
        this.controlNumber = controlNumber;
        this.cancelControlNumber = "";
        this.price = price;
        this.status = PaymentStatus.PAYMENT;
        this.vat = vat;
        this.installment = installment;
        //this.encryptCardInfo = aes256Utils.encrypt(cardNumber + "|" + expiryDate + "|" + cvc);
        this.encryptCardInfo = AES256Utils.encrypt(cardNumber + "|" + expiryDate + "|" + cvc);
        this.stringData = stringData;
    }


    public static Payment convertDtoToPayment(PayRequestDto dto, String controlNumber, String stringData) throws GeneralSecurityException, UnsupportedEncodingException {
        return Payment.builder()
                .controlNumber(controlNumber)
                .price(dto.getPrice())
                .vat(dto.getVat().get())
                .installment(dto.getInstallment())
                .cardNumber(dto.getCardNumber())
                .expiryDate(dto.getExpiryDate())
                .cvc(dto.getCvc())
                .stringData(stringData)
                .build();
    }

    public static PayResponseDto toPayResponseDto(Payment payment){
        return PayResponseDto.builder()
                .controlNumber(payment.getControlNumber())
                .stringData(payment.getStringData())
                .build();
    }

    // 카드번호 마스킹
    private static String maskCardNumber(String cardNumber){
        StringBuilder masking = new StringBuilder(cardNumber);
        masking.replace(6, cardNumber.length() - 3, "*");
        return masking.toString();
    }

    public static PaymentListResponseDto toPaymentListDto(Payment payment) throws GeneralSecurityException, UnsupportedEncodingException {
        CardInfo cardInfo = new CardInfo(AES256Utils.decrypt(payment.getEncryptCardInfo()));

        return PaymentListResponseDto.builder()
                .controlNumber(payment.getControlNumber())
                .cardNumber(maskCardNumber(cardInfo.getCardNumber()))
                .cvc(cardInfo.getCvc())
                .expiryDate(cardInfo.getExpiryDate())
                .price(payment.getPrice())
                .status(payment.getStatus().toString())
                .vat(payment.getVat())
                .build();
    }

    // 전체 취소할 때
    public void cancelPayment(String cancelControlNumber, String stringData){
        this.status = PaymentStatus.CANCEL;
        this.installment = 0;
        this.cancelControlNumber = cancelControlNumber;
        this.stringData = stringData;

    }


    // 취소가 가능한지 확인
    public Boolean isPossibleCancel(int price, int vat){
        if(price <= this.price && vat <= this.vat && this.price-price >= this.vat - vat){
            return true;
        }
        return false;
    }

    // 부분 결제 취소 시
    public void setPartialCancel(int price, int vat, String cancelControlNumber, String stringData){
        this.status = PaymentStatus.PARTIAL_CANCEL;
        this.installment = 0;
        this.cancelControlNumber = cancelControlNumber;
        this.stringData = stringData;
        this.price -= price;
        this.vat -= vat;

        if(this.price == 0 && this.vat == 0){
            this.status = PaymentStatus.CANCEL;
        }
    }





}
