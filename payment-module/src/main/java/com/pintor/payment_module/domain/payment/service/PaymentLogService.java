package com.pintor.payment_module.domain.payment.service;

import com.pintor.payment_module.domain.payment.repository.PaymentLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PaymentLogService {

    private final PaymentLogRepository paymentLogRepository;
}
