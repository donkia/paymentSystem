package com.kakaoinsurance.payment.repository;

import com.kakaoinsurance.payment.domain.Payment;
import com.kakaoinsurance.payment.util.AES256Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PaymentRepositoryTest {

    @Autowired
    PaymentRepository paymentRepository;


    @Test
    void 데이터저장() throws GeneralSecurityException, UnsupportedEncodingException {

        Payment payment = createPayment();

        Payment savedPayment = paymentRepository.save(payment);

        Assertions.assertEquals(1, paymentRepository.findAll().size());
        Assertions.assertEquals(payment.getId(), savedPayment.getId());

    }

    public Payment createPayment() throws GeneralSecurityException, UnsupportedEncodingException {
        return  Payment.builder()
                .cardNumber("123456789012345")
                .cvc("123")
                .price(5000)
                .expiryDate("1121")
                .installment(0)
                .stringData("stringdata")
                .vat(500)
                .build();

    }
    @Test
    void 데이터조회() throws GeneralSecurityException, UnsupportedEncodingException {

        Payment payment = createPayment();
        paymentRepository.save(payment);

        Optional<Payment> optionalPayment = paymentRepository.findByControlNumber(payment.getControlNumber());

        Assertions.assertEquals(true, optionalPayment.isPresent());
        Assertions.assertEquals(payment.getId(), optionalPayment.get().getId());

    }

    @Test
    void 관리번호_중복데이터_저장_테스트() throws GeneralSecurityException, UnsupportedEncodingException {
        Payment payment = new Payment();
        paymentRepository.save(payment);

        Payment payment2 = createPayment();
        paymentRepository.save(payment2);

    }

    @Test
    void 제약조건확인() throws GeneralSecurityException, UnsupportedEncodingException {
        Payment payment = Payment.builder()
                .price(11000)
                .cvc("111")
                .installment(1)
                .vat(1000)
                .cardNumber("1234567890123456")
                .stringData("")
                .expiryDate("1124")
                .controlNumber("a1234567890123456789")
                .build();

        paymentRepository.save(payment);

        Payment sameControlNumberPayment = Payment.builder()
                .price(11000)
                .cvc("111")
                .installment(1)
                .vat(1000)
                .cardNumber("1234567890123456")
                .stringData("")
                .expiryDate("1124")
                .controlNumber("a1234567890123456789")
                .build();
        assertThrows(RuntimeException.class, () -> paymentRepository.save(sameControlNumberPayment));

    }
}