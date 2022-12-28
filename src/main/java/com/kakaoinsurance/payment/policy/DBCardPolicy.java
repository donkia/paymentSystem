package com.kakaoinsurance.payment.policy;

import com.kakaoinsurance.payment.domain.DeliveryCardCompany;
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
        DeliveryCardCompany deliveryCardCompany = DeliveryCardCompany.builder()
                .controlNumber(payment.getControlNumber())
                .stringData(payment.getStringData())
                .build();

        cardRepository.save(deliveryCardCompany);

        return true;
    }

    @Override
    public Boolean sendCancelInfo(String controlNumber, String stringData) {

        DeliveryCardCompany deliveryCardCompany = DeliveryCardCompany.builder()
                .controlNumber(controlNumber)
                .stringData(stringData)
                .build();
        cardRepository.save(deliveryCardCompany);

        return true;
    }
}
