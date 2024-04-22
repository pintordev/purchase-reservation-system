package com.pintor.purchase_reservation_system.domain.product_module.product.entity;

import com.pintor.purchase_reservation_system.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
public class Product extends BaseEntity {

    private String name;

    private Integer price;

    private String description;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime openedAt;
}
