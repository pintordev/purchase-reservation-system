package com.pintor.purchase_reservation_system.domain.member_module.member.entity;

import com.pintor.purchase_reservation_system.common.entity.BaseEntity;
import com.pintor.purchase_reservation_system.domain.member_module.member.converter.MemberConverter;
import com.pintor.purchase_reservation_system.domain.member_module.member.role.MemberRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
    private String phoneNumber;

    @Convert(converter = MemberConverter.class)
    private String zoneCode;

    @Convert(converter = MemberConverter.class)
    private String address;

    @Convert(converter = MemberConverter.class)
    private String subAddress;

    private boolean emailVerified;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<MemberRole> authorities = List.of(this.getRole());
        return authorities.stream()
                .map(a -> new SimpleGrantedAuthority(a.getType()))
                .collect(Collectors.toList());
    }
}
