package com.pintor.member_module.domain.member.controller;

import com.pintor.member_module.domain.member.response.MemberPrincipalResponse;
import com.pintor.member_module.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping(value = "/api/internal/members", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@RestController
public class InternalMemberController {

    private final MemberService memberService;

    @GetMapping(value = "/{id}", consumes = MediaType.ALL_VALUE)
    public MemberPrincipalResponse getMemberPrincipal(@PathVariable(value = "id") Long id) {
        log.info("get member principal: id={}", id);
        return this.memberService.getMemberPrincipal(id);
    }
}
