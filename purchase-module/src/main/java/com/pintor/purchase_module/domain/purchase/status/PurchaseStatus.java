package com.pintor.purchase_module.domain.purchase.status;

public enum PurchaseStatus {

    PURCHASED("purchased"),
    CANCELLED("cancelled"),
    ON_DELIVERY("on_delivery"),
    DELIVERED("delivered"),
    ON_RETURN("on_return"),
    RETURNED("returned");

    private String status;

    PurchaseStatus(String status) {
        this.status = status;
    }

    public static boolean isValid(String status) {
        try {
            PurchaseStatus.valueOf(status);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
