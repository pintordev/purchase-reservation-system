package com.pintor.api_gateway.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ResData<T> {

    @JsonIgnore
    private final HttpStatus status;
    @JsonIgnore
    private final boolean success;
    private final String code;
    private final String message;

    private ResData(FailCode failCode) {
        this.status = failCode.getStatus();
        this.success = false;
        this.code = failCode.getCode();
        this.message = failCode.getMessage();
    }

    public static ResData of(FailCode failCode) {
        return new ResData<>(failCode);
    }
}
