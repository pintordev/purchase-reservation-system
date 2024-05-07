package com.pintor.purchase_module.domain.cart_item.service;

import com.pintor.purchase_module.api.product_module.product.client.ProductClient;
import com.pintor.purchase_module.api.product_module.product.response.ProductResponse;
import com.pintor.purchase_module.common.errors.exception.ApiResException;
import com.pintor.purchase_module.common.response.FailCode;
import com.pintor.purchase_module.common.response.ResData;
import com.pintor.purchase_module.domain.cart.entity.Cart;
import com.pintor.purchase_module.domain.cart.service.CartService;
import com.pintor.purchase_module.domain.cart_item.entity.CartItem;
import com.pintor.purchase_module.domain.cart_item.repository.CartItemRepository;
import com.pintor.purchase_module.domain.cart_item.request.CartItemCreateRequest;
import com.pintor.purchase_module.domain.cart_item.request.CartItemUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    private final CartService cartService;
    private final ProductClient productClient;

    @Transactional
    public void create(CartItemCreateRequest request, BindingResult bindingResult, Long memberId) {

        this.createValidate(bindingResult);

        ProductResponse response = productClient.getProduct(request.getProductId());

        Cart cart = this.cartService.getCart(memberId);

        CartItem cartItem = CartItem.builder()
                .cart(cart)
                .productId(response.productId())
                .name(response.name())
                .price(response.price())
                .quantity(request.getQuantity())
                .build();

        this.cartItemRepository.save(cartItem);
    }

    private void createValidate(BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            log.error("cart item create request validation failed: {}", bindingResult);

            throw new ApiResException(
                    ResData.of(
                            FailCode.BINDING_ERROR,
                            bindingResult
                    )
            );
        }
    }

    @Transactional
    public void update(Long id, CartItemUpdateRequest request, BindingResult bindingResult, Long memberId) {

        CartItem cartItem = this.getCartItem(id);

        this.updateValidate(cartItem, bindingResult, memberId);

        cartItem = cartItem.toBuilder()
                .quantity(request.getQuantity() != null ? request.getQuantity() : cartItem.getQuantity())
                .selected(request.getSelected() != null ? request.getSelected() : cartItem.isSelected())
                .build();

        this.cartItemRepository.save(cartItem);
    }

    private void updateValidate(CartItem cartItem, BindingResult bindingResult, Long memberId) {

        if (bindingResult.hasErrors()) {

            log.error("cart item update request validation failed: {}", bindingResult);

            throw new ApiResException(
                    ResData.of(
                            FailCode.BINDING_ERROR,
                            bindingResult
                    )
            );
        }

        if (cartItem.getCart().getMemberId() != memberId) {

            bindingResult.rejectValue("id", "forbidden", "forbidden access to cart item that does not belong to member");

            throw new ApiResException(
                    ResData.of(
                            FailCode.FORBIDDEN,
                            bindingResult
                    )
            );
        }
    }

    private CartItem getCartItem(Long id) {
        return this.cartItemRepository.findById(id)
                .orElseThrow(() -> new ApiResException(
                        ResData.of(
                                FailCode.CART_ITEM_NOT_FOUND
                        )
                ));
    }

    @Transactional
    public void delete(Long id, Long memberId) {
        CartItem cartItem = this.getCartItem(id);
        this.deleteValidate(cartItem, memberId);
        this.cartItemRepository.delete(cartItem);
    }

    private void deleteValidate(CartItem cartItem, Long memberId) {

        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "deleteCartItem");

        if (cartItem.getCart().getMemberId() != memberId) {

            bindingResult.rejectValue("id", "forbidden", "forbidden access to cart item that does not belong to member");

            throw new ApiResException(
                    ResData.of(
                            FailCode.FORBIDDEN,
                            bindingResult
                    )
            );
        }
    }

    @Transactional
    public void deleteAll(List<CartItem> cartItemList, String type) {
        cartItemList = cartItemList.stream()
                .filter(cartItem -> type.equals("all") || cartItem.isSelected())
                .collect(Collectors.toList());

        this.cartItemRepository.deleteAllInBatch(cartItemList);
    }

    public List<CartItem> getAllByCart(Cart cart) {
        return this.cartItemRepository.findAllByCart(cart);
    }

    public CartItem getCartItemById(Long cartItemId) {
        return this.cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ApiResException(
                        ResData.of(
                                FailCode.CART_ITEM_NOT_FOUND
                        )
                ));
    }
}
