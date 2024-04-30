package com.pintor.purchase_module.domain.cart_item.service;

import com.pintor.purchase_module.common.errors.exception.ApiResException;
import com.pintor.purchase_module.common.principal.MemberPrincipal;
import com.pintor.purchase_module.common.response.FailCode;
import com.pintor.purchase_module.common.response.ResData;
import com.pintor.purchase_module.domain.cart.entity.Cart;
import com.pintor.purchase_module.domain.cart.service.CartService;
import com.pintor.purchase_module.domain.cart_item.entity.CartItem;
import com.pintor.purchase_module.domain.cart_item.repository.CartItemRepository;
import com.pintor.purchase_module.domain.cart_item.request.CartItemCreateRequest;
import com.pintor.purchase_module.domain.cart_item.request.CartItemUpdateRequest;
import com.pintor.purchase_module.domain.cart_item.response.ProductResponse;
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

    @Transactional
    public void create(CartItemCreateRequest request, BindingResult bindingResult, MemberPrincipal principal) {

        this.createValidate(bindingResult);

        // TODO: feign client로 product module 호출
        ProductResponse response = null;

        Cart cart = this.cartService.getCart(principal);

        CartItem cartItem = CartItem.builder()
                .cart(cart)
                .productId(response.getProductId())
                .name(response.getName())
                .price(response.getPrice())
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
    public void update(Long id, CartItemUpdateRequest request, BindingResult bindingResult, MemberPrincipal principal) {

        CartItem cartItem = this.getCartItem(id);

        this.updateValidate(cartItem.getCart().getMemberId(), bindingResult, principal);

        cartItem = cartItem.toBuilder()
                .quantity(request.getQuantity() != null ? request.getQuantity() : cartItem.getQuantity())
                .selected(request.getSelected() != null ? request.getSelected() : cartItem.isSelected())
                .build();

        this.cartItemRepository.save(cartItem);
    }

    private void updateValidate(Long memberId, BindingResult bindingResult, MemberPrincipal principal) {

        if (bindingResult.hasErrors()) {

            log.error("cart item update request validation failed: {}", bindingResult);

            throw new ApiResException(
                    ResData.of(
                            FailCode.BINDING_ERROR,
                            bindingResult
                    )
            );
        }

        if (memberId != principal.getId()) {

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
    public void delete(Long id, MemberPrincipal principal) {
        CartItem cartItem = this.getCartItem(id);
        this.deleteValidate(cartItem.getCart().getMemberId(), principal);
        this.cartItemRepository.delete(cartItem);
    }

    private void deleteValidate(Long memberId, MemberPrincipal principal) {

        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "deleteCartItem");

        if (memberId != principal.getId()) {

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
