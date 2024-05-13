package com.pintor.payment_module.domain.payment.repository;

import com.pintor.payment_module.domain.payment.entity.PaymentLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentLogRepository extends JpaRepository<PaymentLog, Long>  {
}
