package com.pintor.payment_module.domain.payment.repository;

import com.pintor.payment_module.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
