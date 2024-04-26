package com.pintor.purchase_reservation_system.domain.purchase_module.purchase.request;

import com.pintor.purchase_reservation_system.common.config.AppConfig;
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
public class PurchaseCreateUnitRequest {

    @NotBlank(message = "Type is required")
    private String type;

    private Long cartItemId;

    private Long productId;

    private Integer quantity;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = AppConfig.PHONE_NUMBER_REGEX, message = "Invalid phone number format")
    private String phoneNumber;

    @NotBlank(message = "Zone code is required")
    private String zoneCode;

    @NotBlank(message = "Address is required")
    private String address;

    private String subAddress;
}
