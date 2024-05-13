package com.pintor.payment_module.domain.payment.entity;

import com.pintor.payment_module.common.entity.BaseEntity;
import com.pintor.payment_module.domain.payment.status.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "payment", indexes = {
        @Index(name = "status", columnList = "status")
})
public class Payment extends BaseEntity {

    private Long memberId;

    private Long purchaseId;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255)")
    private PaymentStatus status;
}
