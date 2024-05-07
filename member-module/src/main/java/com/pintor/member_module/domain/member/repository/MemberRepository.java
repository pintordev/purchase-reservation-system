package com.pintor.member_module.domain.member.repository;

import com.pintor.member_module.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByEmailAndNameAndPhoneNumber(String email, String name, String phoneNumber);
}
