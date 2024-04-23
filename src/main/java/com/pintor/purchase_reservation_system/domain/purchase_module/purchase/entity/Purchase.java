package com.pintor.purchase_reservation_system.domain.purchase_module.purchase.entity;

import com.pintor.purchase_reservation_system.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
public class Purchase extends BaseEntity {
}
