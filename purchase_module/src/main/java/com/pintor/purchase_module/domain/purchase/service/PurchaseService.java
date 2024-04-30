package com.pintor.purchase_module.domain.purchase.service;

import com.pintor.purchase_module.common.errors.exception.ApiResException;
import com.pintor.purchase_module.common.principal.MemberPrincipal;
import com.pintor.purchase_module.common.response.FailCode;
import com.pintor.purchase_module.common.response.ResData;
import com.pintor.purchase_module.common.util.AddressUtil;
import com.pintor.purchase_module.domain.cart.entity.Cart;
import com.pintor.purchase_module.domain.cart.service.CartService;
import com.pintor.purchase_module.domain.cart_item.entity.CartItem;
import com.pintor.purchase_module.domain.cart_item.service.CartItemService;
import com.pintor.purchase_module.domain.purchase.entity.Purchase;
import com.pintor.purchase_module.domain.purchase.repository.PurchaseBulkRepository;
import com.pintor.purchase_module.domain.purchase.repository.PurchaseRepository;
import com.pintor.purchase_module.domain.purchase.request.PurchaseCreateRequest;
import com.pintor.purchase_module.domain.purchase.status.PurchaseStatus;
import com.pintor.purchase_module.domain.purchase_item.entity.PurchaseItem;
import com.pintor.purchase_module.domain.purchase_item.service.PurchaseItemService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseBulkRepository purchaseBulkRepository;

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final PurchaseItemService purchaseItemService;
    private final PurchaseLogService purchaseLogService;
    private final AddressUtil addressUtil;

    private final EntityManager entityManager;

    private Purchase refresh(Purchase purchase) {
        this.entityManager.flush();
        purchase = this.getPurchaseById(purchase.getId());
        this.entityManager.refresh(purchase);
        return purchase;
    }

    private Purchase getPurchaseById(Long id) {
        return this.purchaseRepository.findById(id)
                .orElseThrow(() -> new ApiResException(
                        ResData.of(
                                FailCode.PURCHASE_NOT_FOUND
                        )
                ));
    }

    @Transactional
    public Purchase create(PurchaseCreateRequest request, BindingResult bindingResult, MemberPrincipal principal) {

        this.createValidate(request, bindingResult);

        Cart cart = this.cartService.getCart(principal);
        List<CartItem> cartItemList = this.cartItemService.getAllByCart(cart);
        int totalPrice = cartItemList.stream()
                .mapToInt(cartItem -> cartItem.getPrice() * cartItem.getQuantity())
                .sum();

        Purchase purchase = Purchase.builder()
                .memberId(principal.getId())
                .status(PurchaseStatus.PURCHASED)
                .totalPrice(totalPrice)
                .phoneNumber(request.getPhoneNumber())
                .zoneCode(request.getZoneCode())
                .address(request.getAddress())
                .subAddress(request.getSubAddress() == null ? "" : request.getSubAddress())
                .build();

        this.purchaseRepository.save(purchase);

        this.purchaseItemService.createAll(cartItemList, purchase, request.getType());
        this.purchaseLogService.log(purchase);
        this.cartItemService.deleteAll(cartItemList, request.getType());
        // this.stockService.decreaseAll(cartItemList);
        // TODO: feign client로 변경

        return this.refresh(purchase);
    }

    private void createValidate(PurchaseCreateRequest request, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            throw new ApiResException(
                    ResData.of(
                            FailCode.BINDING_ERROR,
                            bindingResult
                    )
            );
        }

        if (!request.getType().equals("all") && !request.getType().equals("selected")) {

            bindingResult.rejectValue("type", "invalid purchase type", "purchase type must be all or selected");

            throw new ApiResException(
                    ResData.of(
                            FailCode.INVALID_PURCHASE_TYPE,
                            bindingResult
                    )
            );
        }

        if (!this.addressUtil.isValidAddress(request.getZoneCode(), request.getAddress())) {

            bindingResult.rejectValue("address", "invalid address", "invalid address");

            log.error("invalid address: {}", bindingResult);

            throw new ApiResException(
                    ResData.of(
                            FailCode.INVALID_ADDRESS,
                            bindingResult
                    )
            );
        }
    }

//    @Transactional
//    public Purchase createUnit(PurchaseCreateUnitRequest request, BindingResult bindingResult, MemberPrincipal principal) {
//
//        this.createUnitValidate(request, bindingResult);
//
//        Integer totalPrice;
//        Product product = null;
//        CartItem cartItem = null;
//        if (request.getType().equals("product")) {
//            product = this.productService.getProductDetail(request.getProductId());
//            totalPrice = product.getPrice() * request.getQuantity();
//        } else {
//            cartItem = this.cartItemService.getCartItemById(request.getCartItemId());
//            totalPrice = cartItem.getPrice() * cartItem.getQuantity();
//        }
//
//        Purchase purchase = Purchase.builder()
//                .memberId(principal.getId())
//                .status(PurchaseStatus.PURCHASED)
//                .totalPrice(totalPrice)
//                .phoneNumber(request.getPhoneNumber())
//                .zoneCode(request.getZoneCode())
//                .address(request.getAddress())
//                .subAddress(request.getSubAddress() == null ? "" : request.getSubAddress())
//                .build();
//
//        this.purchaseRepository.save(purchase);
//
//        if (cartItem != null) {
//            this.purchaseItemService.create(request, purchase, cartItem);
//            this.cartItemService.delete(request.getCartItemId(), principal);
//        } else {
//            this.purchaseItemService.create(request, purchase, product);
//        }
//        this.purchaseLogService.log(purchase);
//        // this.stockService.decrease(product, request.getQuantity());
//        // TODO: feign client로 변경
//
//        return this.refresh(purchase);
//    }
//
//    private void createUnitValidate(PurchaseCreateUnitRequest request, BindingResult bindingResult) {
//
//        if (bindingResult.hasErrors()) {
//
//            throw new ApiResException(
//                    ResData.of(
//                            FailCode.BINDING_ERROR,
//                            bindingResult
//                    )
//            );
//        }
//
//        if (!request.getType().equals("product") && !request.getType().equals("cartItem")) {
//
//            bindingResult.rejectValue("type", "invalid purchase type", "purchase type must be product or cartItem");
//
//            throw new ApiResException(
//                    ResData.of(
//                            FailCode.INVALID_PURCHASE_TYPE,
//                            bindingResult
//                    )
//            );
//        }
//
//        if (request.getType().equals("product") && (request.getProductId() == null || request.getQuantity() == null)) {
//
//            bindingResult.rejectValue("productId", "product id is required", "product id is required");
//            bindingResult.rejectValue("quantity", "quantity is required", "quantity is required");
//
//            throw new ApiResException(
//                    ResData.of(
//                            FailCode.PRODUCT_ID_AND_QUANTITY_REQUIRED,
//                            bindingResult
//                    )
//            );
//        }
//
//        if (request.getType().equals("cartItem") && request.getCartItemId() == null) {
//
//            bindingResult.rejectValue("cartItemId", "cart item id is required", "cart item id is required");
//
//            throw new ApiResException(
//                    ResData.of(
//                            FailCode.CART_ITEM_ID_REQUIRED,
//                            bindingResult
//                    )
//            );
//        }
//
//        if (!this.addressUtil.isValidAddress(request.getZoneCode(), request.getAddress())) {
//
//            bindingResult.rejectValue("address", "invalid address", "invalid address");
//
//            log.error("invalid address: {}", bindingResult);
//
//            throw new ApiResException(
//                    ResData.of(
//                            FailCode.INVALID_ADDRESS,
//                            bindingResult
//                    )
//            );
//        }
//    }

    @Transactional
    public void changeStatus() {
        LocalDateTime now = LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0));
        LocalDateTime start = now.minusDays(2);
        LocalDateTime end = now.minusDays(1);

        // 주문 후 D+1에 배송중
        List<Purchase> purchaseList = this.purchaseRepository.findByStatusAndUpdatedAtBetween(PurchaseStatus.PURCHASED, start, end);
        this.purchaseBulkRepository.saveAllWithStatus(purchaseList, PurchaseStatus.ON_DELIVERY, now);
        this.purchaseLogService.log(purchaseList, PurchaseStatus.ON_DELIVERY, now);

        log.info("updated {} purchases to ON_DELIVERY", purchaseList.size());

        // 배송중 후 D+1일에 배송완료
        purchaseList = this.purchaseRepository.findByStatusAndUpdatedAtBetween(PurchaseStatus.ON_DELIVERY, start, end);
        this.purchaseBulkRepository.saveAllWithStatus(purchaseList, PurchaseStatus.DELIVERED, now);
        this.purchaseLogService.log(purchaseList, PurchaseStatus.DELIVERED, now);

        log.info("updated {} purchases to DELIVERED", purchaseList.size());

        // 반품 신청 후 D+1에 반품완료
        purchaseList = this.purchaseRepository.findByStatusAndUpdatedAtBetween(PurchaseStatus.ON_RETURN, start, end);
        this.purchaseBulkRepository.saveAllWithStatus(purchaseList, PurchaseStatus.RETURNED, now);
        this.purchaseLogService.log(purchaseList, PurchaseStatus.RETURNED, now);

        log.info("updated {} purchases to RETURNED", purchaseList.size());

        // 반품완료 후 재고 반영
        List<PurchaseItem> purchaseItemList = this.purchaseItemService.getAllByPurchaseList(purchaseList);
        // this.stockService.increaseAll(purchaseItemList);
        // TODO: feign client로 변경

        log.info("reverted stocks");
    }

    public Page<Purchase> getPurchaseList(int page, int size, String sort, String dir, String status, MemberPrincipal principal) {

        this.getPurchaseListValidate(page, size, sort, dir, status);

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(new Sort.Order(Sort.Direction.fromString(dir), sort));

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sorts));

        if (status.equals("ALL")) {
            return this.purchaseRepository.findAllByMemberId(principal.getId(), pageable);
        } else {
            return this.purchaseRepository.findAllByMemberIdAndStatus(principal.getId(), PurchaseStatus.valueOf(status), pageable);
        }
    }

    private void getPurchaseListValidate(int page, int size, String sort, String dir, String status) {

        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "purchaseList");

        if (size < 1) {

            bindingResult.rejectValue("size", "invalid size", "size must be greater than 0");

            throw new ApiResException(
                    ResData.of(
                            FailCode.INVALID_SIZE,
                            bindingResult
                    )
            );
        }

        if (page < 1 || this.purchaseRepository.count() / size < page - 1) {

            bindingResult.rejectValue("page", "invalid page", "page does not exist");

            throw new ApiResException(
                    ResData.of(
                            FailCode.INVALID_PAGE,
                            bindingResult
                    )
            );
        }

        if (!sort.equals("createdAt") && !sort.equals("openedAt") && !sort.equals("price")) {

            bindingResult.rejectValue("sort", "invalid sort", "sort is invalid");

            throw new ApiResException(
                    ResData.of(
                            FailCode.INVALID_SORT,
                            bindingResult
                    )
            );
        }

        if (!dir.equals("asc") && !dir.equals("desc")) {

            bindingResult.rejectValue("dir", "invalid dir", "dir is invalid");

            throw new ApiResException(
                    ResData.of(
                            FailCode.INVALID_SORT,
                            bindingResult
                    )
            );
        }

        if (!status.equals("ALL") && !PurchaseStatus.isValid(status)) {

            bindingResult.rejectValue("status", "invalid status", "status is invalid");

            throw new ApiResException(
                    ResData.of(
                            FailCode.INVALID_STATUS,
                            bindingResult
                    )
            );
        }
    }

    public Purchase getPurchaseDetail(Long id, MemberPrincipal principal) {
        Purchase purchase = this.getPurchaseById(id);
        this.getPurchaseDetailValidate(purchase.getMemberId(), principal);
        return purchase;
    }

    private void getPurchaseDetailValidate(Long memberId, MemberPrincipal principal) {

        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "purchaseDetail");

        if (memberId != principal.getId()) {

            bindingResult.rejectValue("id", "forbidden", "forbidden access to purchase that does not belong to member");

            throw new ApiResException(
                    ResData.of(
                            FailCode.FORBIDDEN,
                            bindingResult
                    )
            );
        }
    }

    @Transactional
    public void cancelPurchase(Long id, MemberPrincipal principal) {

        Purchase purchase = this.getPurchaseById(id);

        this.cancelPurchaseValidate(purchase, principal);

        purchase = purchase.toBuilder()
                .status(PurchaseStatus.CANCELLED)
                .build();

        this.purchaseRepository.save(purchase);
        this.purchaseLogService.log(purchase);
        List<PurchaseItem> purchaseItemList = this.purchaseItemService.getAllByPurchase(purchase);
        // this.stockService.increaseAll(purchaseItemList);
        // TODO: feign client로 변경
    }

    private void cancelPurchaseValidate(Purchase purchase, MemberPrincipal principal) {

        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "cancelPurchase");

        if (purchase.getMemberId() != principal.getId()) {

            bindingResult.rejectValue("id", "forbidden", "forbidden access to purchase that does not belong to member");

            throw new ApiResException(
                    ResData.of(
                            FailCode.FORBIDDEN,
                            bindingResult
                    )
            );
        }

        if (purchase.getStatus() != PurchaseStatus.PURCHASED) {

            bindingResult.rejectValue("id", "invalid cancellable status", "purchase is not cancellable status");

            throw new ApiResException(
                    ResData.of(
                            FailCode.INVALID_CANCELLABLE_STATUS,
                            bindingResult
                    )
            );
        }
    }

    @Transactional
    public void returnPurchase(Long id, MemberPrincipal principal) {

        Purchase purchase = this.getPurchaseById(id);

        this.returnPurchaseValidate(purchase, principal);

        purchase = purchase.toBuilder()
                .status(PurchaseStatus.ON_RETURN)
                .build();

        this.purchaseRepository.save(purchase);
        this.purchaseLogService.log(purchase);
    }

    private void returnPurchaseValidate(Purchase purchase, MemberPrincipal principal) {

        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "returnPurchase");

        if (purchase.getMemberId() != principal.getId()) {

            bindingResult.rejectValue("id", "forbidden", "forbidden access to purchase that does not belong to member");

            throw new ApiResException(
                    ResData.of(
                            FailCode.FORBIDDEN,
                            bindingResult
                    )
            );
        }

        if (purchase.getStatus() != PurchaseStatus.DELIVERED || !purchase.getUpdatedAt().isAfter(LocalDateTime.now().minusDays(1))) {

            bindingResult.rejectValue("id", "invalid returnable status", "purchase is not returnable status");

            throw new ApiResException(
                    ResData.of(
                            FailCode.INVALID_RETURNABLE_STATUS,
                            bindingResult
                    )
            );
        }
    }
}
