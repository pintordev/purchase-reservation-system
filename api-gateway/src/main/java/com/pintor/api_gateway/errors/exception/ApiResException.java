package com.pintor.api_gateway.errors.exception;

import com.pintor.api_gateway.response.ResData;
import lombok.Getter;

@Getter
public class ApiResException extends RuntimeException {

    private final ResData resData;

    public ApiResException(ResData resData) {
        super(resData.getMessage());
        this.resData = resData;
    }
}