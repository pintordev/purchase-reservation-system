package com.pintor.purchase_module.domain.cart_item.entity;

import com.pintor.purchase_module.domain.cart.entity.Cart;
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

    private Long productId;

    private String name;

    private Integer price;

    private Integer quantity;

    private boolean selected;
}
