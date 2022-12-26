package com.kakaoinsurance.payment.policy;

import com.kakaoinsurance.payment.domain.CardCompany;
import com.kakaoinsurance.payment.domain.Payment;
import com.kakaoinsurance.payment.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DBCardPolicy implements CardPolicy{

    private final CardRepository cardRepository;


    @Override
    public Boolean sendPayInfo(Payment payment) {
        CardCompany cardCompany = CardCompany.builder()
                .controlNumber(payment.getControlNumber())
                .stringData(payment.getStringData())
                .build();

        cardRepository.save(cardCompany);

        return true;
    }

    @Override
    public Boolean sendCancelInfo(String controlNumber, String stringData) {

        CardCompany cardCompany = CardCompany.builder()
                .controlNumber(controlNumber)
                .stringData(stringData)
                .build();
        cardRepository.save(cardCompany);

        return true;
    }
}
