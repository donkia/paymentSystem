package com.kakaoinsurance.payment.service;

import com.kakaoinsurance.payment.domain.Payment;
import com.kakaoinsurance.payment.domain.PaymentStatus;
import com.kakaoinsurance.payment.exception.CustomException;
import com.kakaoinsurance.payment.exception.ErrorCode;
import com.kakaoinsurance.payment.policy.CardPolicy;
import com.kakaoinsurance.payment.policy.DBCardPolicy;
import com.kakaoinsurance.payment.repository.PaymentRepository;
import com.kakaoinsurance.payment.util.AES256Utils;
import com.kakaoinsurance.payment.util.ControlNumberUtils;
import com.kakaoinsurance.payment.util.StringDataUtils;
import com.kakaoinsurance.payment.web.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PaymentService {

    private final CardPolicy cardPolicy;
    private final PaymentRepository paymentRepository;


    // 부가가치세 계산
    public int calculateVat(int price){
        return (int) Math.round(price / 11.0);
    }

    // 결제
    public PayResponseDto pay(PayRequestDto dto) throws Exception {

        // 부가가치세 계산 : 빈값으로 넘어왔을 때 자동계산
        if(dto.getVat().isEmpty()){
            dto.setVat(Optional.of(calculateVat(dto.getPrice())));
        }


        // 관리번호 생성
        String newControlNumber = ControlNumberUtils.createControlNumber();
        if(paymentRepository.findByControlNumber(newControlNumber).isPresent()){
            // 이미 존재하는 관리번호일 경우
            throw new CustomException(ErrorCode.ALREADY_SAVED_NUMBER);
        }

        // string data 생성
        String stringData = StringDataUtils.setPayStringData(dto, newControlNumber);

        // DB에 데이터 적재
        Payment payment = Payment.convertDtoToPayment(dto, newControlNumber, stringData);
        //log.info(payment.hashCode());

        // 카드사에 전달
        cardPolicy.sendPayInfo(payment);

        // 결제내역 별도로 저장
        Payment savedPayment = paymentRepository.save(payment);

        return PayResponseDto.builder()
                .stringData(payment.getStringData())
                .controlNumber(payment.getControlNumber())
                .build();
    }

    public CancelResponseDto cancel(CancelRequestDto dto) throws GeneralSecurityException, UnsupportedEncodingException {

        Payment payment = paymentRepository.findByControlNumber(dto.getControlNumber()).orElseThrow(()->new CustomException(ErrorCode.NUMBER_NOT_FOUND));
        //Payment payment = paymentRepository.findByControlNumber(dto.getControlNumber()).get();

        // 부가가치세 계산 : 빈값으로 넘어왔을 때 자동계산
        if(dto.getVat().isEmpty()){
            dto.setVat(Optional.of(payment.getVat()));
        }

        log.info("payment : {}", payment);

        if(payment.getStatus().equals(PaymentStatus.CANCEL)){
            // 이미 취소된 데이터인 경우
            throw new CustomException(ErrorCode.ALREADY_CANCEL);
        }

        // 취소 전용 관리번호 생성
        String cancelControlNumber = ControlNumberUtils.createControlNumber();
        if(paymentRepository.findByCancelControlNumber(cancelControlNumber).isPresent()){
            // 이미 존재하는 관리번호일 경우
            throw new CustomException(ErrorCode.ALREADY_SAVED_NUMBER);
        }


        // 취소 stringdata 생성
        String stringData = StringDataUtils.setCancelStringData(dto, payment, cancelControlNumber);

        // 카드사에 전달
        cardPolicy.sendCancelInfo(cancelControlNumber, stringData);

        // 취소내역 별도 DB 저장
        payment.cancelPayment(cancelControlNumber, stringData);

        return CancelResponseDto.builder()
                .stringData(stringData)
                .controlNumber(cancelControlNumber)
                .build();
    }

    public PaymentListResponseDto getPaymentList(String id) throws Exception {
        Optional<Payment> payment = null;
        payment = paymentRepository.findByControlNumber(id);

        if(payment.isEmpty()){
            payment = paymentRepository.findByCancelControlNumber(id);
            if(payment.isEmpty()){
                log.info("관리번호가 존재하지 않습니다.");
                throw new CustomException(ErrorCode.NUMBER_NOT_FOUND);
            }
        }

        PaymentListResponseDto requestDto = Payment.toPaymentListDto(payment.get());

        return requestDto;

    }

}
