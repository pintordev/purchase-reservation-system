package com.pintor.purchase_module.domain.purchase_module.cart_item.service;

import com.pintor.purchase_module.common.errors.exception.ApiResException;
import com.pintor.purchase_module.common.response.FailCode;
import com.pintor.purchase_module.common.response.ResData;
import com.pintor.purchase_module.domain.product_module.product.entity.Product;
import com.pintor.purchase_module.domain.product_module.product.service.ProductService;
import com.pintor.purchase_module.domain.purchase_module.cart.entity.Cart;
import com.pintor.purchase_module.domain.purchase_module.cart.service.CartService;
import com.pintor.purchase_module.domain.purchase_module.cart_item.entity.CartItem;
import com.pintor.purchase_module.domain.purchase_module.cart_item.repository.CartItemRepository;
import com.pintor.purchase_module.domain.purchase_module.cart_item.request.CartItemCreateRequest;
import com.pintor.purchase_module.domain.purchase_module.cart_item.request.CartItemUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    private final CartService cartService;
    private final ProductService productService;

    @Transactional
    public void create(CartItemCreateRequest request, BindingResult bindingResult, User user) {

        this.createValidate(bindingResult);
        Product product = this.productService.getProductDetail(request.getProductId());

        Cart cart = this.cartService.getCart(user);

        CartItem cartItem = CartItem.builder()
                .cart(cart)
                .product(product)
                .name(product.getName())
                .price(product.getPrice())
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
    public void update(Long id, CartItemUpdateRequest request, BindingResult bindingResult) {

        this.updateValidate(bindingResult);

        CartItem cartItem = this.getCartItem(id);

        cartItem = cartItem.toBuilder()
                .quantity(request.getQuantity() != null ? request.getQuantity() : cartItem.getQuantity())
                .selected(request.getSelected() != null ? request.getSelected() : cartItem.isSelected())
                .build();

        this.cartItemRepository.save(cartItem);
    }

    private void updateValidate(BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            log.error("cart item update request validation failed: {}", bindingResult);

            throw new ApiResException(
                    ResData.of(
                            FailCode.BINDING_ERROR,
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
    public void delete(Long id) {
        CartItem cartItem = this.getCartItem(id);
        this.cartItemRepository.delete(cartItem);
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
