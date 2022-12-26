package com.kakaoinsurance.payment.service;

import com.kakaoinsurance.payment.domain.Payment;
import com.kakaoinsurance.payment.domain.PaymentStatus;
import com.kakaoinsurance.payment.policy.CardPolicy;
import com.kakaoinsurance.payment.repository.PaymentRepository;
import com.kakaoinsurance.payment.util.AES256Utils;
import com.kakaoinsurance.payment.util.ControlNumberUtils;
import com.kakaoinsurance.payment.util.StringDataUtils;
import com.kakaoinsurance.payment.web.dto.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private CardPolicy dbCardPolicy;


    @InjectMocks
    private PaymentService paymentService;

    private PayRequestDto createDto(){
        return new PayRequestDto("123456789", "1121", "111", 0, 5500, Optional.of(500));
    }



    @Test
    @DisplayName("결제 서비스 테스트")
    void 결제_테스트() throws Exception {

        // given
        PayRequestDto dto = createDto();
        String controlNumber = ControlNumberUtils.createControlNumber();
        String stringData = StringDataUtils.setPayStringData(dto, controlNumber);
        Payment payment = Payment.convertDtoToPayment(dto, controlNumber, stringData);

        // stub
        lenient().when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        // when
        PayResponseDto responseDto = paymentService.pay(dto);
        System.out.println("dto : " + responseDto.toString());
        // then
        Assertions.assertEquals(20, responseDto.getControlNumber().length());
        Assertions.assertEquals(PaymentStatus.PAYMENT.toString(), responseDto.getStringData().substring(4, 11));
        Assertions.assertEquals(dto.getCvc(), responseDto.getStringData().substring(59, 62));
        Assertions.assertEquals(dto.getExpiryDate(), responseDto.getStringData().substring(56, 60));

    }

    @Test
    @DisplayName("결제 취소 서비스 테스트")
    void 취소_테스트() throws GeneralSecurityException, UnsupportedEncodingException {
        Payment payment = Payment.builder()
                .price(10000)
                .cvc("111")
                .installment(1)
                .vat(100)
                .cardNumber("1234567890123456")
                .stringData("")
                .expiryDate("1124")
                .controlNumber(ControlNumberUtils.createControlNumber())
                .build();

        //stub
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(paymentRepository.findByControlNumber(any(String.class))).thenReturn(Optional.of(payment));

        // when
        paymentRepository.save(payment);
        PaymentService service = new PaymentService(dbCardPolicy, paymentRepository);
        CancelRequestDto dto = new CancelRequestDto(payment.getControlNumber(), payment.getPrice(), Optional.of(payment.getVat()));
        CancelResponseDto responseDto = service.cancel(dto);

        // then
        // string data에 결제 상태가 CANCEL 인지 확인
        Assertions.assertEquals(PaymentStatus.CANCEL.toString(), responseDto.getStringData().substring(4, 10));
        // string data에 결제할 때 관리번호 포함되어있는지 확인
        Assertions.assertEquals(payment.getControlNumber(), responseDto.getStringData().substring(83, 103));

    }

    @Test
    @DisplayName("결제건 조회 서비스 테스트")
    void getPaymentList() throws Exception {

        // given
        PayRequestDto dto = createDto();
        String controlNumber = ControlNumberUtils.createControlNumber();
        String stringData = StringDataUtils.setPayStringData(dto, controlNumber);

        Payment payment = Payment.convertDtoToPayment(dto, controlNumber, stringData);

        // stub
        lenient().when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        lenient().when(paymentRepository.findByControlNumber(any(String.class))).thenReturn(Optional.of(payment));

        // when
        PaymentService service = new PaymentService(dbCardPolicy, paymentRepository);
        paymentRepository.save(payment);

        PaymentListResponseDto paymentListResponseDto = service.getPaymentList(payment.getControlNumber());

        // then
        Assertions.assertEquals(payment.getControlNumber(), paymentListResponseDto.getControlNumber());
        Assertions.assertEquals(payment.getPrice(), paymentListResponseDto.getPrice());


    }

}