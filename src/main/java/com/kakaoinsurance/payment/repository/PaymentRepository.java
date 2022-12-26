package com.kakaoinsurance.payment.repository;

import com.kakaoinsurance.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {


    Optional<Payment> findByControlNumber(String controlNumber);
    Optional<Payment> findByCancelControlNumber(String cancelControlNumber);
}
