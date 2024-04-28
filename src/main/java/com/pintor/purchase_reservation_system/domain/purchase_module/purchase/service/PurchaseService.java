package com.pintor.purchase_reservation_system.domain.purchase_module.purchase.service;

import com.pintor.purchase_reservation_system.common.errors.exception.ApiResException;
import com.pintor.purchase_reservation_system.common.response.FailCode;
import com.pintor.purchase_reservation_system.common.response.ResData;
import com.pintor.purchase_reservation_system.common.service.AddressService;
import com.pintor.purchase_reservation_system.domain.member_module.member.entity.Member;
import com.pintor.purchase_reservation_system.domain.member_module.member.service.MemberService;
import com.pintor.purchase_reservation_system.domain.product_module.product.entity.Product;
import com.pintor.purchase_reservation_system.domain.product_module.product.service.ProductService;
import com.pintor.purchase_reservation_system.domain.product_module.stock.service.StockService;
import com.pintor.purchase_reservation_system.domain.purchase_module.cart.entity.Cart;
import com.pintor.purchase_reservation_system.domain.purchase_module.cart.service.CartService;
import com.pintor.purchase_reservation_system.domain.purchase_module.cart_item.entity.CartItem;
import com.pintor.purchase_reservation_system.domain.purchase_module.cart_item.service.CartItemService;
import com.pintor.purchase_reservation_system.domain.purchase_module.purchase.entity.Purchase;
import com.pintor.purchase_reservation_system.domain.purchase_module.purchase.repository.PurchaseBulkRepository;
import com.pintor.purchase_reservation_system.domain.purchase_module.purchase.repository.PurchaseRepository;
import com.pintor.purchase_reservation_system.domain.purchase_module.purchase.request.PurchaseCreateRequest;
import com.pintor.purchase_reservation_system.domain.purchase_module.purchase.request.PurchaseCreateUnitRequest;
import com.pintor.purchase_reservation_system.domain.purchase_module.purchase.status.PurchaseStatus;
import com.pintor.purchase_reservation_system.domain.purchase_module.purchase_item.service.PurchaseItemService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.User;
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
    private final AddressService addressService;
    private final MemberService memberService;
    private final ProductService productService;
    private final StockService stockService;

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
    public Purchase create(PurchaseCreateRequest request, BindingResult bindingResult, User user) {

        this.createValidate(request, bindingResult);

        Cart cart = this.cartService.getCart(user);
        List<CartItem> cartItemList = this.cartItemService.getAllByCart(cart);
        int totalPrice = cartItemList.stream()
                .mapToInt(cartItem -> cartItem.getPrice() * cartItem.getQuantity())
                .sum();

        Purchase purchase = Purchase.builder()
                .member(cart.getMember())
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
        this.stockService.decreaseAll(cartItemList);

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

        if (!this.addressService.isValidAddress(request.getZoneCode(), request.getAddress())) {

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

    @Transactional
    public Purchase createUnit(PurchaseCreateUnitRequest request, BindingResult bindingResult, User user) {

        this.createUnitValidate(request, bindingResult);

        Member member = this.memberService.getMemberByEmail(user.getUsername());

        Integer totalPrice;
        Product product = null;
        CartItem cartItem = null;
        if (request.getType().equals("product")) {
            product = this.productService.getProductDetail(request.getProductId());
            totalPrice = product.getPrice() * request.getQuantity();
        } else {
            cartItem = this.cartItemService.getCartItemById(request.getCartItemId());
            totalPrice = cartItem.getPrice() * cartItem.getQuantity();
        }

        Purchase purchase = Purchase.builder()
                .member(member)
                .status(PurchaseStatus.PURCHASED)
                .totalPrice(totalPrice)
                .phoneNumber(request.getPhoneNumber())
                .zoneCode(request.getZoneCode())
                .address(request.getAddress())
                .subAddress(request.getSubAddress() == null ? "" : request.getSubAddress())
                .build();

        this.purchaseRepository.save(purchase);

        if (cartItem != null) {
            this.purchaseItemService.create(request, purchase, cartItem);
            this.cartItemService.delete(request.getCartItemId());
        } else {
            this.purchaseItemService.create(request, purchase, product);
        }
        this.purchaseLogService.log(purchase);
        this.stockService.decrease(product, request.getQuantity());

        return this.refresh(purchase);
    }

    private void createUnitValidate(PurchaseCreateUnitRequest request, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            throw new ApiResException(
                    ResData.of(
                            FailCode.BINDING_ERROR,
                            bindingResult
                    )
            );
        }

        if (!request.getType().equals("product") && !request.getType().equals("cartItem")) {

            bindingResult.rejectValue("type", "invalid purchase type", "purchase type must be product or cartItem");

            throw new ApiResException(
                    ResData.of(
                            FailCode.INVALID_PURCHASE_TYPE,
                            bindingResult
                    )
            );
        }

        if (request.getType().equals("product") && (request.getProductId() == null || request.getQuantity() == null)) {

            bindingResult.rejectValue("productId", "product id is required", "product id is required");
            bindingResult.rejectValue("quantity", "quantity is required", "quantity is required");

            throw new ApiResException(
                    ResData.of(
                            FailCode.PRODUCT_ID_AND_QUANTITY_REQUIRED,
                            bindingResult
                    )
            );
        }

        if (request.getType().equals("cartItem") && request.getCartItemId() == null) {

            bindingResult.rejectValue("cartItemId", "cart item id is required", "cart item id is required");

            throw new ApiResException(
                    ResData.of(
                            FailCode.CART_ITEM_ID_REQUIRED,
                            bindingResult
                    )
            );
        }

        if (!this.addressService.isValidAddress(request.getZoneCode(), request.getAddress())) {

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

        // TODO: 반품 신청 후 D+1에 반품완료
    }

    public Page<Purchase> getPurchaseList(int page, int size, String sort, String dir, String status, User user) {

        this.getPurchaseListValidate(page, size, sort, dir, status);

        Member member = this.memberService.getMemberByEmail(user.getUsername());

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(new Sort.Order(Sort.Direction.fromString(dir), sort));

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sorts));

        if (status.equals("ALL")) {
            return this.purchaseRepository.findAllByMember(member, pageable);
        } else {
            return this.purchaseRepository.findAllByMemberAndStatus(member, PurchaseStatus.valueOf(status), pageable);
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

    public Purchase getPurchaseDetail(Long id, User user) {
        Purchase purchase = this.getPurchaseById(id);
        this.getPurchaseDetailValidate(purchase, user);
        return purchase;
    }

    private void getPurchaseDetailValidate(Purchase purchase, User user) {

        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "purchaseDetail");

        if (!purchase.getMember().getEmail().equals(user.getUsername())) {

            bindingResult.rejectValue("id", "forbidden", "forbidden access to purchase that does not belong to user");

            throw new ApiResException(
                    ResData.of(
                            FailCode.FORBIDDEN,
                            bindingResult
                    )
            );
        }
    }

    @Transactional
    public void cancelPurchase(Long id, User user) {

        Purchase purchase = this.getPurchaseById(id);

        this.cancelPurchaseValidate(purchase, user);

        purchase = purchase.toBuilder()
                .status(PurchaseStatus.CANCELLED)
                .build();

        this.purchaseRepository.save(purchase);
        this.purchaseLogService.log(purchase);
        this.stockService.increaseAll(purchase.getPurchaseItemList());
    }

    private void cancelPurchaseValidate(Purchase purchase, User user) {

        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "cancelPurchase");

        if (!purchase.getMember().getEmail().equals(user.getUsername())) {

            bindingResult.rejectValue("id", "forbidden", "forbidden access to purchase that does not belong to user");

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
    public void returnPurchase(Long id, User user) {

        Purchase purchase = this.getPurchaseById(id);

        purchase = purchase.toBuilder()
                .status(PurchaseStatus.ON_RETURN)
                .build();

        this.purchaseRepository.save(purchase);
        this.purchaseLogService.log(purchase);
    }
}
