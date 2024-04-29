package com.pintor.product_module.domain.purchase_module.cart.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.pintor.product_module.common.response.ListedData;
import com.pintor.product_module.domain.purchase_module.cart.entity.Cart;
import com.pintor.product_module.domain.purchase_module.cart_item.response.CartItemResponse;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class CartListResponse {

    @JsonUnwrapped
    private ListedData<CartItemResponse> cartItemList;

    private CartListResponse(Cart cart) {
        this.cartItemList = ListedData.of(cart.getCartItemList(), CartItemResponse::of);
    }

    public static CartListResponse of(Cart cart) {
        return new CartListResponse(cart);
    }
}
