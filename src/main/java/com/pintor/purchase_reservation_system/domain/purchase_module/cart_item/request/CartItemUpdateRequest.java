package com.pintor.purchase_reservation_system.domain.purchase_module.cart_item.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemUpdateRequest {

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    private Boolean selected;
}
