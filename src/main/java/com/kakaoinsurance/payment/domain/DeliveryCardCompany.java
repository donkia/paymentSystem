package com.kakaoinsurance.payment.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryCardCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String controlNumber;

    @Column(length=450)
    private String stringData;

    @Builder
    DeliveryCardCompany(String controlNumber, String stringData){
        this.controlNumber = controlNumber;
        this.stringData = stringData;
    }
}
