package com.pintor.purchase_module.domain.purchase_item.entity;

import com.pintor.purchase_module.domain.purchase.entity.Purchase;
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

    private Long productId;

    private String name;

    private Integer price;

    private Integer quantity;
}
