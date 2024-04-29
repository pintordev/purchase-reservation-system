package com.pintor.member_module.domain.purchase_module.cart_item.entity;

import com.pintor.member_module.domain.product_module.product.entity.Product;
import com.pintor.member_module.domain.purchase_module.cart.entity.Cart;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Entity
@EqualsAndHashCode(of = "id")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private String name;

    private Integer price;

    private Integer quantity;

    private boolean selected;
}
