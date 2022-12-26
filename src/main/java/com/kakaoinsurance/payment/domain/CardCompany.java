package com.kakaoinsurance.payment.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CardCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String controlNumber;

    @Column(length=450)
    private String stringData;

    @Builder
    CardCompany(String controlNumber, String stringData){
        this.controlNumber = controlNumber;
        this.stringData = stringData;
    }
}
