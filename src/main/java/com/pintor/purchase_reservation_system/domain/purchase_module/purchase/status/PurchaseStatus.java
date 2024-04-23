package com.pintor.purchase_reservation_system.domain.purchase_module.purchase.status;

public enum PurchaseStatus {

    PURCHASED("purchased"),
    CANCELLED("cancelled"),
    ON_DELIVERY("on_delivery"),
    DELIVERED("delivered"),
    RETURN("return");

    private String status;

    PurchaseStatus(String status) {
        this.status = status;
    }

}
