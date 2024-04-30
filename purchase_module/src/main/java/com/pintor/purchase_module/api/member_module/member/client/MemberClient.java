package com.pintor.purchase_module.api.member_module.member.client;

import com.pintor.purchase_module.api.member_module.member.response.MemberPrincipalResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "memberClient", url = "http://localhost:8081/api/internal/members")
public interface MemberClient {

    @GetMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    MemberPrincipalResponse getMemberPrincipal(@PathVariable(value = "id") Long id, @RequestHeader("Authorization") String token);
}