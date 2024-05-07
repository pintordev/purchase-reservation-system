package com.pintor.purchase_module.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@RequiredArgsConstructor
@Service
public class AddressUtil {

    @Value("${kakao.api.key}")
    private String KAKAO_API_KEY;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public boolean isValidAddress(String zoneCode, String address) {

        String response = this.webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("dapi.kakao.com")
                        .path("/v2/local/search/address.json")
                        .queryParam("query", address)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "KakaoAK " + KAKAO_API_KEY)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        log.info("kakao response: {}", response);

        try {
            JsonNode root = this.objectMapper.readTree(response);
            JsonNode documents = root.path("documents");
            for (JsonNode document : documents) {
                if (document.path("road_address").path("zone_no").asText().equals(zoneCode)) {
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("Error parsing response: {}", e.getMessage());
        }

        return false;
    }
}
