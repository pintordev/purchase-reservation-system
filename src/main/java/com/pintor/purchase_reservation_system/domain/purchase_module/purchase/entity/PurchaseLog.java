package com.pintor.purchase_reservation_system.domain.purchase_module.purchase.entity;

import com.pintor.purchase_reservation_system.common.converter.AesConverter;
import com.pintor.purchase_reservation_system.domain.member_module.member.entity.Member;
import com.pintor.purchase_reservation_system.domain.purchase_module.purchase.status.PurchaseStatus;
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
public class PurchaseLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Purchase purchase;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255)")
    private PurchaseStatus status;

    private Integer totalPrice;

    @Convert(converter = AesConverter.class)
    private String phoneNumber;

    @Convert(converter = AesConverter.class)
    private String zoneCode;

    @Convert(converter = AesConverter.class)
    private String address;

    @Convert(converter = AesConverter.class)
    private String subAddress;

    @Column(columnDefinition = "TIMESTAMP")
    @CreatedDate
    private LocalDateTime createdAt;
}
