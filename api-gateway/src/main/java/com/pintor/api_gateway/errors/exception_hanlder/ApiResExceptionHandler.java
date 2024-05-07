package com.pintor.api_gateway.errors.exception_hanlder;

import com.pintor.api_gateway.errors.exception.ApiResException;
import com.pintor.api_gateway.response.FailCode;
import com.pintor.api_gateway.response.ResData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiResExceptionHandler {

    @ExceptionHandler({ApiResException.class})
    public ResponseEntity handleApiResException(ApiResException e) {
        ResData resData = e.getResData();
        log.error("[Request Error: {}] {}", resData.getCode(), resData.getMessage());
        return ResponseEntity
                .status(resData.getStatus())
                .body(resData);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity handleRuntimeException(RuntimeException e) {
        log.error("exception: ", e.toString());
        log.error("cause: ", e.getCause().toString());

        ResData resData = ResData.of(
                FailCode.INTERNAL_SERVER_ERROR
        );
        return ResponseEntity
                .status(resData.getStatus())
                .body(resData);
    }
}
