package com.kakaoinsurance.payment.web;

import com.kakaoinsurance.payment.service.PaymentService;
import com.kakaoinsurance.payment.web.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/api/v1/pay")
    public ResponseEntity<?> pay(@RequestBody @Valid PayRequestDto dto, BindingResult bindingResult) throws Exception {

        log.info("dto : {}", dto.toString());
        if(bindingResult.hasErrors()){
            throw new IllegalArgumentException(bindingResult.getFieldError().getDefaultMessage());
        }

        if(dto.getVat().isPresent() && dto.getPrice() < dto.getVat().get()){
            throw new IllegalArgumentException("부가가치세가 결제 금액보다 클 수 없습니다");
        }

        PayResponseDto responseDto = paymentService.pay(dto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);

    }

    @PutMapping("/api/v1/pay")
    public ResponseEntity<?> cancel(@RequestBody @Valid CancelRequestDto dto, BindingResult bindingResult) throws Exception {

        if(bindingResult.hasErrors()){
            throw new IllegalArgumentException(bindingResult.getFieldError().getDefaultMessage());
        }

        CancelResponseDto responseDto = paymentService.cancel(dto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/api/v1/pay/{id}")
    public ResponseEntity<?> getPaymentList(@PathVariable(value = "id") String id) throws Exception {

        PaymentListResponseDto responseDto = paymentService.getPaymentList(id);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);

    }

}
