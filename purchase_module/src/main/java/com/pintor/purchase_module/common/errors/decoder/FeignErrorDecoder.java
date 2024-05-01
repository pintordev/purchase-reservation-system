package com.pintor.purchase_module.common.errors.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pintor.purchase_module.common.errors.exception.ApiResException;
import com.pintor.purchase_module.common.response.ResData;
import com.pintor.purchase_module.common.response.ResError;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        String body = "";
        if (response.body() != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().asInputStream(), StandardCharsets.UTF_8))) {
                body = reader.lines().collect(Collectors.joining("\n"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Map<String, Object> bodyMap = toMap(body);
        HttpStatus status = HttpStatus.valueOf(response.status());
        String code = bodyMap.get("code").toString();
        String message = bodyMap.get("message").toString();
        List<ResError> errors = (List<ResError>) bodyMap.get("data");
        return new ApiResException(
                ResData.of(status, code, message, errors)
        );
    }

    private Map<String, Object> toMap(String body) {
        try {
            return this.objectMapper.readValue(body, LinkedHashMap.class);
        } catch (IOException e) {
            return null;
        }
    }
}
