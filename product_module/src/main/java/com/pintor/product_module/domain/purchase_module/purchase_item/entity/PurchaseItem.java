package com.pintor.product_module.domain.purchase_module.purchase_item.entity;

import com.pintor.product_module.domain.product_module.product.entity.Product;
import com.pintor.product_module.domain.purchase_module.purchase.entity.Purchase;
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
public class PurchaseItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Purchase purchase;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private String name;

    private Integer price;

    private Integer quantity;
}
