package com.pintor.payment_module.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ResData<T> {

    @JsonIgnore
    private final HttpStatus status;
    @JsonIgnore
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

    public static ResData of(SuccessCode successCode) {
        return new ResData<>(successCode, null);
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

    public ResData(HttpStatus status, String code, String message, List<ResError> errors) {
        this.status = status;
        this.success = false;
        this.code = code;
        this.message = message;
        this.data = (T) errors;
    }

    public static ResData of(HttpStatus status, String code, String message, List<ResError> errors) {
        return new ResData<>(status, code, message, errors);
    }
}
