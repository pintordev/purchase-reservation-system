package com.pintor.member_module.domain.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthVerifyMailRequest {

    @NotBlank(message = "Code is required")
    private String code;
}
