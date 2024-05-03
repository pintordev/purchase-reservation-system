package com.pintor.purchase_module.api.member_module.member.client;

import com.pintor.purchase_module.api.member_module.member.response.MemberPrincipalResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "memberClient", url = "http://localhost:8081/api/internal")
public interface MemberClient {

    @PostMapping(value = "/auth/passport", consumes = MediaType.APPLICATION_JSON_VALUE)
    MemberPrincipalResponse getMemberPrincipal(@RequestHeader("X-Server-Token") String serverToken, @RequestHeader("Authorization") String accessToken);
}