package com.kakaoinsurance.payment.policy;

import com.kakaoinsurance.payment.domain.CardCompany;
import com.kakaoinsurance.payment.domain.Payment;

public interface CardPolicy {

    // 결제
    Boolean sendPayInfo(Payment payment);

    // 취소
    Boolean sendCancelInfo(String controlNumber, String stringData);
}
