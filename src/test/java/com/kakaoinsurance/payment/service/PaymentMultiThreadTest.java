package com.kakaoinsurance.payment.service;

import com.kakaoinsurance.payment.domain.Payment;
import com.kakaoinsurance.payment.repository.PaymentRepository;
import com.kakaoinsurance.payment.web.dto.CancelRequestDto;
import com.kakaoinsurance.payment.web.dto.CancelResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
class PaymentMultiThreadTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    @Transactional
    void 스레드테스트() throws GeneralSecurityException, UnsupportedEncodingException, InterruptedException {
        AtomicInteger successCount = new AtomicInteger();
        int numberOfExcute =10;
        ExecutorService service = Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(numberOfExcute);

        Payment payment = Payment.builder()
                .price(10000)
                .cvc("111")
                .installment(1)
                .vat(100)
                .cardNumber("1234567890123456")
                .stringData("")
                .expiryDate("1124")
                .controlNumber("a1234567890123456789")
                .build();
        paymentRepository.save(payment);

        CancelRequestDto dto = new CancelRequestDto(payment.getControlNumber(), payment.getPrice(), Optional.of(payment.getVat()));


        for(int i = 0; i < numberOfExcute; i++){
            int finalI = i;
            service.execute(()->{
                        try{

                            Optional<Payment> pay = paymentRepository.findByControlNumber(payment.getControlNumber());
                            successCount.getAndIncrement();
                            System.out.println(finalI +", : " + pay.toString());
                            //pay.get().
                        }catch (ObjectOptimisticLockingFailureException oe){
                            System.out.println("충돌 감지");
                        }catch (Exception e){
                            System.out.println(finalI + "exception : " + e.getMessage());
                        }
                        latch.countDown();

                    }

            );
        }
        latch.await();



    }

    @Test
    void 부분결제_테스트_멀티쓰레드() throws GeneralSecurityException, UnsupportedEncodingException, InterruptedException {
        AtomicInteger successCount = new AtomicInteger();
        int numberOfExcute =100;
        ExecutorService service = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(numberOfExcute);

        Payment payment = Payment.builder()
                .price(110000)
                .cvc("111")
                .installment(1)
                .vat(10000)
                .cardNumber("1234567890123456")
                .stringData("")
                .expiryDate("1124")
                .controlNumber("a1234567890123456789")
                .build();
        paymentRepository.save(payment);

        CancelRequestDto dto = new CancelRequestDto(payment.getControlNumber(), 1100, Optional.of(100));


        for(int i = 0; i < numberOfExcute; i++){
            int finalI = i;
            service.execute(()->{
                        try{
                            //CancelResponseDto responseDto = paymentService.partialCancel(dto);
                            paymentService.partialCancelByRedisson(dto);

                            successCount.getAndIncrement();
                        }catch (ObjectOptimisticLockingFailureException oe){
                            System.out.println("충돌 감지");
                        }catch (Exception e){
                            System.out.println("exception : " + e.getMessage());
                        }
                latch.countDown();
                    }

            );
        }
        latch.await();



    }
}
