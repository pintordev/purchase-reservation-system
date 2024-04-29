package com.pintor.purchase_module.domain.purchase.controller;

import com.pintor.purchase_module.common.response.ResData;
import com.pintor.purchase_module.common.response.SuccessCode;
import com.pintor.purchase_module.domain.purchase.entity.Purchase;
import com.pintor.purchase_module.domain.purchase.response.PurchaseCreateResponse;
import com.pintor.purchase_module.domain.purchase.response.PurchaseListResponse;
import com.pintor.purchase_module.domain.purchase.request.PurchaseCreateRequest;
import com.pintor.purchase_module.domain.purchase.request.PurchaseCreateUnitRequest;
import com.pintor.purchase_module.domain.purchase.service.PurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(consumes = MediaType.ALL_VALUE)
    public ResponseEntity purchaseList(@RequestParam(value = "page", defaultValue = "1") int page,
                                       @RequestParam(value = "size", defaultValue = "20") int size,
                                       @RequestParam(value = "sort", defaultValue = "createdAt") String sort,
                                       @RequestParam(value = "dir", defaultValue = "desc") String dir,
                                       @RequestParam(value = "status", defaultValue = "all") String status,
                                       @AuthenticationPrincipal User user) {

        log.info("purchase list request: page={}, size={}, sort={}, dir={}, status={}", page, size, sort, dir, status);

        Page<Purchase> purchaseList = this.purchaseService.getPurchaseList(page, size, sort, dir, status.toUpperCase(), user);

        ResData resData = ResData.of(
                SuccessCode.PURCHASE_LIST,
                PurchaseListResponse.of(purchaseList)
        );
        return ResponseEntity
                .status(resData.getStatus())
                .body(resData);
    }

    @GetMapping(value = "/{id}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity purchaseDetail(@PathVariable(value = "id") Long id,
                                         @AuthenticationPrincipal User user) {

        log.info("purchase detail request: id={}", id);

        Purchase purchase = this.purchaseService.getPurchaseDetail(id, user);

        ResData resData = ResData.of(
                SuccessCode.PURCHASE_DETAIL,
                PurchaseCreateResponse.of(purchase)
        );
        return ResponseEntity
                .status(resData.getStatus())
                .body(resData);
    }

    @PatchMapping(value = "/{id}/cancel", consumes = MediaType.ALL_VALUE)
    public ResponseEntity cancelPurchase(@PathVariable(value = "id") Long id,
                                         @AuthenticationPrincipal User user) {

        log.info("purchase cancel request: id={}", id);

        this.purchaseService.cancelPurchase(id, user);

        ResData resData = ResData.of(
                SuccessCode.CANCEL_PURCHASE
        );
        return ResponseEntity
                .status(resData.getStatus())
                .body(resData);
    }

    @PatchMapping(value = "/{id}/return", consumes = MediaType.ALL_VALUE)
    public ResponseEntity returnPurchase(@PathVariable(value = "id") Long id,
                                         @AuthenticationPrincipal User user) {

        log.info("purchase return request: id={}", id);

        this.purchaseService.returnPurchase(id, user);

        ResData resData = ResData.of(
                SuccessCode.RETURN_PURCHASE
        );
        return ResponseEntity
                .status(resData.getStatus())
                .body(resData);
    }
}