package com.pintor.product_module.domain.purchase_module.purchase.request;

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
public class PurchaseCreateRequest {

    @NotBlank(message = "Type is required")
    private String type;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = AppConfig.PHONE_NUMBER_REGEX, message = "Invalid phone number format")
    private String phoneNumber;

    @NotBlank(message = "Zone code is required")
    private String zoneCode;

    @NotBlank(message = "Address is required")
    private String address;

    private String subAddress;
}