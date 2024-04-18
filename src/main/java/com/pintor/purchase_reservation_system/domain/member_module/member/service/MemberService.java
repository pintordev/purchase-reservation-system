package com.pintor.purchase_reservation_system.domain.member_module.member.service;

import com.pintor.purchase_reservation_system.domain.member_module.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
}
