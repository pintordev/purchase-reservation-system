package com.pintor.purchase_reservation_system.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

@Getter
public class ResData<T> {

    private final HttpStatus status;
    private final boolean success;
    private final String code;
    private final String message;
    private final T data;

    private ResData(SuccessCode successCode, T data) {
        this.status = successCode.getStatus();
        this.success = true;
        this.code = successCode.getCode();
        this.message = successCode.getMessage();
        this.data = data;
    }

    public static <T> ResData<T> of(SuccessCode successCode, T data) {
        return new ResData<>(successCode, data);
    }

    private ResData(FailCode failCode, T data) {
        this.status = failCode.getStatus();
        this.success = false;
        this.code = failCode.getCode();
        this.message = failCode.getMessage();
        this.data = data;
    }

    public static ResData of(FailCode failCode, BindingResult bindingResult) {
        return new ResData<>(failCode, bindingResult);
    }

    public static ResData of(FailCode failCode) {
        return new ResData<>(failCode, null);
    }
}
