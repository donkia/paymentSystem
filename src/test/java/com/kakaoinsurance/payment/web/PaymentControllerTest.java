package com.kakaoinsurance.payment.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.kakaoinsurance.payment.domain.Payment;
import com.kakaoinsurance.payment.domain.PaymentStatus;
import com.kakaoinsurance.payment.repository.PaymentRepository;
import com.kakaoinsurance.payment.web.dto.CancelRequestDto;
import com.kakaoinsurance.payment.web.dto.PayRequestDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentControllerTest {

    @Autowired
    private TestRestTemplate rt;

    @Autowired
    private PaymentRepository paymentRepository;


    private static ObjectMapper om;
    private static HttpHeaders headers;

    @BeforeAll
    public static void init(){
        om = new ObjectMapper();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

    }

    @BeforeEach
    public void 데이터준비() throws GeneralSecurityException, UnsupportedEncodingException {

        Payment payment = Payment.builder()
                .cardNumber("123456789012345")
                .cvc("123")
                .price(5000)
                .expiryDate("1121")
                .installment(0)
                .stringData("stringdata")
                .vat(500)
                .build();

        paymentRepository.save(payment);


    }

    @Test
    @DisplayName("결제 통합 테스트")
    @Order(1)
    void pay() throws JsonProcessingException {

        // given
        PayRequestDto dto = new PayRequestDto("012345678901234", "1123", "123",
                2, 1000);
        String body = om.writeValueAsString(dto);

        // when
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = rt.exchange("/api/v1/pay", HttpMethod.POST, request, String.class);
        System.out.println(response.toString());
        ObjectMapper mapper = new ObjectMapper();

        // then
        DocumentContext dc = JsonPath.parse(response.getBody());
        String stringData = dc.read("stringData");
        String controlNumber = dc.read("controlNumber");
        Assertions.assertEquals(450, stringData.length());
        Assertions.assertEquals(20, controlNumber.length());

    }

    // 결제 데이터 생성
    void pay_test() throws JsonProcessingException {
        // given
        PayRequestDto dto = new PayRequestDto("012345678901234", "1123", "123",
                2, 1000);
        String body = om.writeValueAsString(dto);

        // when
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = rt.exchange("/api/v1/pay", HttpMethod.POST, request, String.class);

    }

    @Test
    @DisplayName("결제취소 통합테스트")
    @Order(2)
    void cancel() throws JsonProcessingException {
        pay_test();
        Payment payment = paymentRepository.findAll().get(1);

        // given
        CancelRequestDto dto = new CancelRequestDto(payment.getControlNumber(), payment.getPrice());
        String body = om.writeValueAsString(dto);
        System.out.println(payment.toString());

        // when
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = rt.exchange("/api/v1/pay", HttpMethod.PUT, request, String.class);

        ObjectMapper mapper = new ObjectMapper();
        System.out.println("response : " + response.getBody());

        // then
        DocumentContext dc = JsonPath.parse(response.getBody());
        String stringData = dc.read("stringData");
        String controlNumber = dc.read("controlNumber");

        Assertions.assertEquals(450, stringData.length());
        Assertions.assertEquals(20, controlNumber.length());
        Assertions.assertEquals(PaymentStatus.CANCEL.toString() ,stringData.substring(4,10));
        Assertions.assertNotEquals(payment.getControlNumber() ,controlNumber);

    }


    @Test
    @DisplayName("결제 목록 통합테스트")
    @Order(3)
    void getPaymentList() throws JsonProcessingException {
        // given

        Payment payment = paymentRepository.findAll().get(1);

        // when
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/pay/" + payment.getControlNumber(),
                HttpMethod.GET, request, String.class);

        // then
        DocumentContext dc = JsonPath.parse(response.getBody());
        String controlNumber = dc.read("controlNumber");
        String status = dc.read("status");
        int price = dc.read("price");

        Assertions.assertEquals(20, controlNumber.length());
        Assertions.assertEquals(payment.getPrice(), price);

    }
}