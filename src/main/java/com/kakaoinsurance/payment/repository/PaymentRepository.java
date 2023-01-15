package com.kakaoinsurance.payment.repository;

import com.kakaoinsurance.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Payment> findByControlNumber(String controlNumber);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Payment> findByCancelControlNumber(String cancelControlNumber);

}
