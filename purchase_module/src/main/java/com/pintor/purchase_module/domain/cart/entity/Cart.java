package com.pintor.purchase_module.domain.cart.entity;

import com.pintor.member_module.domain.member.entity.Member;
import com.pintor.purchase_module.domain.cart_item.entity.CartItem;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Entity
@EqualsAndHashCode(of = "id")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "cart", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CartItem> cartItemList;
}
