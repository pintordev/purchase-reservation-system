package com.pintor.payment_module.domain.payment.entity;

import com.pintor.payment_module.domain.payment.status.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Entity
@EqualsAndHashCode(of = "id")
public class PaymentLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Payment payment;

    private Long memberId;

    private Long purchaseId;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255)")
    private PaymentStatus status;

    @Column(columnDefinition = "TIMESTAMP")
    @CreatedDate
    private LocalDateTime createdAt;
}
