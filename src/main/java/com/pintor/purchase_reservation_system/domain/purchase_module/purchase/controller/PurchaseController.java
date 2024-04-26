package com.pintor.purchase_reservation_system.domain.purchase_module.purchase.controller;

import com.pintor.purchase_reservation_system.common.response.ResData;
import com.pintor.purchase_reservation_system.common.response.SuccessCode;
import com.pintor.purchase_reservation_system.domain.purchase_module.purchase.entity.Purchase;
import com.pintor.purchase_reservation_system.domain.purchase_module.purchase.request.PurchaseCreateRequest;
import com.pintor.purchase_reservation_system.domain.purchase_module.purchase.request.PurchaseCreateUnitRequest;
import com.pintor.purchase_reservation_system.domain.purchase_module.purchase.response.PurchaseCreateResponse;
import com.pintor.purchase_reservation_system.domain.purchase_module.purchase.service.PurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping(value = "/api/purchases", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@RestController
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping
    public ResponseEntity createPurchase(@Valid @RequestBody PurchaseCreateRequest request, BindingResult bindingResult,
                                         @AuthenticationPrincipal User user) {

        log.info("purchase create request: {}", request);

        Purchase purchase = this.purchaseService.create(request, bindingResult, user);

        ResData resData = ResData.of(
                SuccessCode.CREATE_PURCHASE,
                PurchaseCreateResponse.of(purchase)
        );
        return ResponseEntity
                .status(resData.getStatus())
                .body(resData);
    }

    @PostMapping(value = "/unit")
    public ResponseEntity createPurchaseUnit(@Valid @RequestBody PurchaseCreateUnitRequest request, BindingResult bindingResult,
                                             @AuthenticationPrincipal User user) {

        log.info("purchase create request: {}", request);

        Purchase purchase = this.purchaseService.createUnit(request, bindingResult, user);

        ResData resData = ResData.of(
                SuccessCode.CREATE_PURCHASE_UNIT,
                PurchaseCreateResponse.of(purchase)
        );
        return ResponseEntity
                .status(resData.getStatus())
                .body(resData);
    }
}
