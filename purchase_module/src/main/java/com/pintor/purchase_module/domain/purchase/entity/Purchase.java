package com.pintor.purchase_module.domain.purchase.entity;

import com.pintor.purchase_module.common.converter.AesConverter;
import com.pintor.purchase_module.common.entity.BaseEntity;
import com.pintor.purchase_module.domain.member_module.member.entity.Member;
import com.pintor.purchase_module.domain.purchase_item.entity.PurchaseItem;
import com.pintor.purchase_module.domain.purchase.status.PurchaseStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "purchase", indexes = {
        @Index(name = "status", columnList = "status")
})
public class Purchase extends BaseEntity {

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

    @OneToMany(mappedBy = "purchase", fetch = FetchType.LAZY)
    private List<PurchaseItem> purchaseItemList;
}
