package com.pintor.product_module.domain.member_module.member.request;

import com.pintor.product_module.common.config.AppConfig;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSignupRequest {

    @NotBlank(message = "Email is required")
    @Pattern(regexp = AppConfig.EMAIL_REGEX, message = "Invalid email format")
    private String email;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = AppConfig.PASSWORD_REGEX, message = "Password must contain at least 8 characters, one uppercase, one lowercase, one number and one special character")
    private String password;

    @NotBlank(message = "Password confirmation is required")
    private String passwordConfirm;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = AppConfig.PHONE_NUMBER_REGEX, message = "Invalid phone number format")
    private String phoneNumber;

    @NotBlank(message = "Zone code is required")
    private String zoneCode;

    @NotBlank(message = "Address is required")
    private String address;

    private String subAddress;

    private boolean isAdmin;
}
