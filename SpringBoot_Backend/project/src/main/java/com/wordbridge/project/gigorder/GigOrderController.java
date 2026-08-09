package com.wordbridge.project.gigorder;

import com.wordbridge.project.dto.responsedto.UserProfileResponseDTO;
import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.GigOrderStatus;
import com.wordbridge.project.enums.UserRole;
import com.wordbridge.project.gig.GigResponseDTO;
import com.wordbridge.project.gig.GigService;
import com.wordbridge.project.security.AuthenticationService;
import com.wordbridge.project.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/api/gig-orders")
@RequiredArgsConstructor
public class GigOrderController {

    private final GigOrderService gigOrderService;

    private final UserProfileService userProfileService;
    private final GigService gigService;
    private final AuthenticationService authenticationService;

    @PreAuthorize("hasRole('USER') or hasRole('COMPANY')")
    @PostMapping
    public ResponseEntity<GigOrderResponseDTO> placeOrder(
            @RequestParam Long gigId,
            @RequestParam Long buyerId) {

        User currentUser = authenticationService.getCurrentUser();
        if (!currentUser.getId().equals(buyerId)) {
            throw new AccessDeniedException("Not allowed");
        }

        return ResponseEntity.ok(gigOrderService.placeOrder(gigId, buyerId));
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/{orderId}/quote")
    public ResponseEntity<GigOrderResponseDTO> sendQuote(
            @PathVariable Long orderId,
            @RequestParam BigDecimal quotedPrice) {
        checkSellerOwnership(orderId);

        return ResponseEntity.ok(gigOrderService.sendQuote(orderId, quotedPrice));
    }

    @PreAuthorize("hasRole('USER') or hasRole('COMPANY')")
    @PatchMapping("/{orderId}/accept-quote")
    public ResponseEntity<GigOrderResponseDTO> acceptQuote(
            @PathVariable Long orderId) {
        checkBuyerOwnership(orderId);

        return ResponseEntity.ok(gigOrderService.acceptQuote(orderId));
    }

    @PreAuthorize("hasRole('USER') or hasRole('COMPANY')")
    @PatchMapping("/{orderId}/reject-quote")
    public ResponseEntity<GigOrderResponseDTO> rejectQuote(
            @PathVariable Long orderId) {

        checkBuyerOwnership(orderId);
        return ResponseEntity.ok(gigOrderService.rejectQuote(orderId));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{orderId}/deliver")
    public ResponseEntity<GigOrderResponseDTO> deliverOrder(
            @PathVariable Long orderId,
            @RequestParam(required = false) String deliveryMessage,
            @RequestPart MultipartFile deliveryFile) {

        checkSellerOwnership(orderId);
        return ResponseEntity.ok(
                gigOrderService.deliverOrder(orderId, deliveryMessage, deliveryFile)
        );
    }

    @PreAuthorize("hasRole('USER') or hasRole('COMPANY')")
    @PatchMapping("/{orderId}/accept-delivery")
    public ResponseEntity<GigOrderResponseDTO> acceptDelivery(
            @PathVariable Long orderId) {
        checkBuyerOwnership(orderId);

        return ResponseEntity.ok(gigOrderService.acceptDelivery(orderId));
    }

    @PreAuthorize("hasRole('USER') or hasRole('COMPANY')")
    @PatchMapping("/{orderId}/reject-delivery")
    public ResponseEntity<GigOrderResponseDTO> rejectDelivery(
            @PathVariable Long orderId) {
        checkBuyerOwnership(orderId);

        return ResponseEntity.ok(gigOrderService.rejectDelivery(orderId));
    }

    @PreAuthorize("hasRole('USER') or hasRole('COMPANY')")
    @PatchMapping("/{orderId}/buyer-cancel")
    public ResponseEntity<GigOrderResponseDTO> buyerCancel(
            @PathVariable Long orderId) {
        checkBuyerOwnership(orderId);

        return ResponseEntity.ok(gigOrderService.buyerCancelOrder(orderId));
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/{orderId}/seller-cancel")
    public ResponseEntity<GigOrderResponseDTO> sellerCancel(
            @PathVariable Long orderId) {

        checkSellerOwnership(orderId);
        return ResponseEntity.ok(gigOrderService.cancelOrderBySeller(orderId));
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/{orderId}/dispute")
    public ResponseEntity<GigOrderResponseDTO> raiseDispute(
            @PathVariable Long orderId) {

        checkSellerOwnership(orderId);
        return ResponseEntity.ok(gigOrderService.raiseDispute(orderId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{orderId}/release-payment")
    public ResponseEntity<GigOrderResponseDTO> releasePayment(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(gigOrderService.releasePayment(orderId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{orderId}/refund")
    public ResponseEntity<GigOrderResponseDTO> refundBuyer(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(gigOrderService.refundBuyer(orderId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<GigOrderResponseDTO>> getAll() {

        return ResponseEntity.ok(gigOrderService.getAll());
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<GigOrderResponseDTO> getById(
            @PathVariable Long id) {

        checkOrderAccess(id);
        return ResponseEntity.ok(gigOrderService.getById(id));
    }

    @PreAuthorize("hasRole('USER') or hasRole('COMPANY')")
    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<GigOrderResponseDTO>> getBuyerOrders(
            @PathVariable Long buyerId) {
        checkBuyerIdOwnership(buyerId);

        return ResponseEntity.ok(gigOrderService.getBuyerOrders(buyerId));
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<GigOrderResponseDTO>> getSellerOrders(
            @PathVariable Long sellerId) {

        checkSellerProfileIdOwnershipOrAdmin(sellerId);
        return ResponseEntity.ok(gigOrderService.getSellerOrders(sellerId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<GigOrderResponseDTO>> getByStatus(
            @PathVariable GigOrderStatus status) {

        return ResponseEntity.ok(gigOrderService.getByStatus(status));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/buyer/{buyerId}/status/{status}")
    public ResponseEntity<List<GigOrderResponseDTO>> findByBuyerIdAndStatus(
            @PathVariable Long buyerId,
            @PathVariable GigOrderStatus status) {

        checkBuyerIdOwnership(buyerId);
        return ResponseEntity.ok(
                gigOrderService.findByBuyerIdAndStatus(buyerId, status)
        );
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/buyer/{buyerId}/status/{status}/count")
    public Long countByBuyerIdAndStatus(
            @PathVariable Long buyerId,
            @PathVariable GigOrderStatus status) {

        checkBuyerIdOwnership(buyerId);
        return gigOrderService.countByBuyerIdAndStatus(buyerId, status);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/seller/{sellerId}/status/{status}")
    public ResponseEntity<List<GigOrderResponseDTO>> findByGigUserProfileIdAndStatus(
            @PathVariable Long sellerId,
            @PathVariable GigOrderStatus status) {
        checkSellerProfileIdOwnershipOrAdmin(sellerId);
        return ResponseEntity.ok(
                gigOrderService.findByGigUserProfileIdAndStatus(sellerId, status)
        );
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/seller/{sellerId}/status/{status}/count")
    public Long countByGigUserProfileIdAndStatus(
            @PathVariable Long sellerId,
            @PathVariable GigOrderStatus status) {

        checkSellerProfileIdOwnershipOrAdmin(sellerId);
        return gigOrderService.countByGigUserProfileIdAndStatus(sellerId, status);
    }


    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/gig/{gigId}")
    public ResponseEntity<List<GigOrderResponseDTO>> findByGigId(
            @PathVariable Long gigId) {

        checkGigOwnershipOrAdmin(gigId);
        return ResponseEntity.ok(
                gigOrderService.findByGigId(gigId)
        );
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/gig/{gigId}/count")
    public ResponseEntity<Long> countByGigId(
            @PathVariable Long gigId) {
        checkGigOwnershipOrAdmin(gigId);
        return ResponseEntity.ok(
                gigOrderService.countByGigId(gigId)
        );
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/seller/{sellerId}/count")
    public ResponseEntity<Long> countByGigUserProfileId(
            @PathVariable Long sellerId) {

        checkSellerProfileIdOwnershipOrAdmin(sellerId);
        return ResponseEntity.ok(
                gigOrderService.countByGigUserProfileId(sellerId)
        );
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/buyer/{buyerId}/count")
    public ResponseEntity<Long> countByBuyerId(
            @PathVariable Long buyerId) {

        checkBuyerIdOwnership(buyerId);
        return ResponseEntity.ok(
                gigOrderService.countByBuyerId(buyerId)
        );
    }

    @PreAuthorize("hasRole('USER') or hasRole('COMPANY')")
    @GetMapping("/gig/{gigId}/buyer/{buyerId}/exist")
    public Boolean existsByGigIdAndBuyerId(@PathVariable Long gigId, @PathVariable Long buyerId) {
        checkBuyerIdOwnership(buyerId);
        return gigOrderService.existsByGigIdAndBuyerId(gigId, buyerId);
    }

    @PreAuthorize("hasRole('USER') or hasRole('COMPANY')")
    @GetMapping("/gig/{gigId}/buyer/{buyerId}/active")
    public ResponseEntity<GigOrderResponseDTO> findActiveOrder(
            @PathVariable Long gigId,
            @PathVariable Long buyerId
    ) {

        checkBuyerIdOwnership(buyerId);
        return ResponseEntity.ok(
                gigOrderService.findActiveOrder(
                        gigId,
                        buyerId
                )
        );

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("search")
    public List<GigOrderResponseDTO> search(
            @RequestBody GigOrderFilterRequestDTO dto
    ) {

        return gigOrderService.search(dto);

    }


    //  Private

    private void checkBuyerIdOwnership(Long buyerId) {
        User currentUser = authenticationService.getCurrentUser();
        if (!currentUser.getId().equals(buyerId) && currentUser.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Not allowed");
        }
    }

    private void checkSellerProfileIdOwnershipOrAdmin(Long sellerId) {
        User currentUser = authenticationService.getCurrentUser();
        if (currentUser.getRole() == UserRole.ADMIN) return;
        UserProfileResponseDTO profile = userProfileService.findById(sellerId);
        if (!profile.getUserId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Not allowed");
        }
    }

    private void checkGigOwnershipOrAdmin(Long gigId) {
        User currentUser = authenticationService.getCurrentUser();
        if (currentUser.getRole() == UserRole.ADMIN) return;
        GigResponseDTO gig = gigService.getById(gigId);
        checkSellerProfileIdOwnershipOrAdmin(gig.getUserProfileId());
    }

    private void checkBuyerOwnership(Long orderId) {
        User currentUser = authenticationService.getCurrentUser();
        GigOrderResponseDTO order = gigOrderService.getById(orderId);
        if (!order.getBuyerId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Not allowed");
        }
    }

    private void checkSellerOwnership(Long orderId) {
        User currentUser = authenticationService.getCurrentUser();
        GigOrderResponseDTO order = gigOrderService.getById(orderId);
        UserProfileResponseDTO sellerProfile = userProfileService.findById(order.getSellerId());
        if (!sellerProfile.getUserId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Not allowed");
        }
    }

    private void checkBuyerOrSellerOwnership(Long orderId) {
        User currentUser = authenticationService.getCurrentUser();
        GigOrderResponseDTO order = gigOrderService.getById(orderId);
        boolean isBuyer = order.getBuyerId().equals(currentUser.getId());
        boolean isSeller = userProfileService.findById(order.getSellerId()).getUserId().equals(currentUser.getId());
        if (!isBuyer && !isSeller) {
            throw new AccessDeniedException("Not allowed");
        }
    }

    private void checkOrderAccess(Long orderId) {
        User currentUser = authenticationService.getCurrentUser();
        if (currentUser.getRole() == UserRole.ADMIN) return;
        checkBuyerOrSellerOwnership(orderId);
    }


}