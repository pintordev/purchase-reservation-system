package com.pintor.member_module.domain.purchase_module.cart_item.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pintor.member_module.domain.purchase_module.cart_item.entity.CartItem;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class CartItemResponse {

    private final Long productId;
    private final String name;
    private final Integer price;
    private final Integer quantity;
    private final Integer totalPrice;
    private final boolean selected;

    private CartItemResponse(CartItem cartItem) {
        this.productId = cartItem.getProduct().getId();
        this.name = cartItem.getName();
        this.price = cartItem.getPrice();
        this.quantity = cartItem.getQuantity();
        this.totalPrice = this.price * this.quantity;
        this.selected = cartItem.isSelected();
    }

    public static CartItemResponse of(CartItem cartItem) {
        return new CartItemResponse(cartItem);
    }
}