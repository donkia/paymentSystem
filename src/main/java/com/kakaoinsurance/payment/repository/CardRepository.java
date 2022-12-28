package com.kakaoinsurance.payment.repository;

import com.kakaoinsurance.payment.domain.DeliveryCardCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<DeliveryCardCompany, Long> {
}
