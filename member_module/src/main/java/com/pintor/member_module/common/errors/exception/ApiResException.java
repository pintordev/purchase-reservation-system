package com.pintor.member_module.common.errors.exception;

import com.pintor.member_module.common.response.ResData;
import lombok.Getter;

@Getter
public class ApiResException extends RuntimeException {

    private final ResData resData;

    public ApiResException(ResData resData) {
        super(resData.getMessage());
        this.resData = resData;
    }
}