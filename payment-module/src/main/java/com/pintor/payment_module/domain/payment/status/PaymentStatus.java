package com.pintor.payment_module.domain.payment.status;

public enum PaymentStatus {

    PENDING("pending"),
    PAID("paid"),
    FAILED("failed");

    private String status;

    PaymentStatus(String status) {
        this.status = status;
    }
}
