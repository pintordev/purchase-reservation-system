package com.pintor.purchase_reservation_system.domain.member_module.auth.request;

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
