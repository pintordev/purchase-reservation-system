package com.pintor.purchase_reservation_system.domain.member_module.member.entity;

import com.pintor.purchase_reservation_system.common.entity.BaseEntity;
import com.pintor.purchase_reservation_system.domain.member_module.member.role.MemberRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
public class Member extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255)")
    private MemberRole role;

    @Column(unique = true)
    private String email;

    private String name;

    private String password;

    private String address;

    private boolean emailVerified;
}
