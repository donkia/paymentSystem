package com.kakaoinsurance.payment.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Payment extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number; //관리번호

    private Long price; //가격

    private String status;  //상태

    private Long vat; //부가가치세

    private int installment; //할부개월수

    @Embedded
    private CardInfo cardInfo; //카드정보

    @Builder
    public Payment(String number, Long price, String status, Long vat, int installment, CardInfo cardInfo) {
        this.number = number;
        this.price = price;
        this.status = status;
        this.vat = vat;
        this.installment = installment;
        this.cardInfo = cardInfo;
    }
}
