package com.pintor.purchase_reservation_system.domain.member_module.member.entity;

import com.pintor.purchase_reservation_system.common.entity.BaseEntity;
import com.pintor.purchase_reservation_system.domain.member_module.member.converter.MemberConverter;
import com.pintor.purchase_reservation_system.domain.member_module.member.role.MemberRole;
import jakarta.persistence.*;
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

    @Convert(converter = MemberConverter.class)
    @Column(unique = true)
    private String email;

    @Convert(converter = MemberConverter.class)
    private String name;

    private String password;

    @Convert(converter = MemberConverter.class)
    private String address;

    private boolean emailVerified;
}
