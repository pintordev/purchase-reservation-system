package com.pintor.purchase_reservation_system.domain.member_module.member.repository;

import com.pintor.purchase_reservation_system.domain.member_module.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
}
