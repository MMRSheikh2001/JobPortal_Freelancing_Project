package com.wordbridge.project.gigorder;

import com.wordbridge.project.enums.GigOrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/api/gig-orders")
@RequiredArgsConstructor
public class GigOrderController {

    private final GigOrderService gigOrderService;

    @PostMapping
    public ResponseEntity<GigOrderResponseDTO> placeOrder(
            @RequestParam Long gigId,
            @RequestParam Long buyerId) {

        return ResponseEntity.ok(gigOrderService.placeOrder(gigId, buyerId));
    }

    @PatchMapping("/{orderId}/quote")
    public ResponseEntity<GigOrderResponseDTO> sendQuote(
            @PathVariable Long orderId,
            @RequestParam BigDecimal quotedPrice) {

        return ResponseEntity.ok(gigOrderService.sendQuote(orderId, quotedPrice));
    }

    @PatchMapping("/{orderId}/accept-quote")
    public ResponseEntity<GigOrderResponseDTO> acceptQuote(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(gigOrderService.acceptQuote(orderId));
    }

    @PatchMapping("/{orderId}/reject-quote")
    public ResponseEntity<GigOrderResponseDTO> rejectQuote(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(gigOrderService.rejectQuote(orderId));
    }

    @PostMapping("/{orderId}/deliver")
    public ResponseEntity<GigOrderResponseDTO> deliverOrder(
            @PathVariable Long orderId,
            @RequestParam(required = false) String deliveryMessage,
            @RequestPart MultipartFile deliveryFile) {

        return ResponseEntity.ok(
                gigOrderService.deliverOrder(orderId, deliveryMessage, deliveryFile)
        );
    }

    @PatchMapping("/{orderId}/accept-delivery")
    public ResponseEntity<GigOrderResponseDTO> acceptDelivery(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(gigOrderService.acceptDelivery(orderId));
    }

    @PatchMapping("/{orderId}/reject-delivery")
    public ResponseEntity<GigOrderResponseDTO> rejectDelivery(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(gigOrderService.rejectDelivery(orderId));
    }

    @PatchMapping("/{orderId}/buyer-cancel")
    public ResponseEntity<GigOrderResponseDTO> buyerCancel(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(gigOrderService.buyerCancelOrder(orderId));
    }

    @PatchMapping("/{orderId}/seller-cancel")
    public ResponseEntity<GigOrderResponseDTO> sellerCancel(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(gigOrderService.cancelOrderBySeller(orderId));
    }

    @PatchMapping("/{orderId}/dispute")
    public ResponseEntity<GigOrderResponseDTO> raiseDispute(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(gigOrderService.raiseDispute(orderId));
    }

    @PatchMapping("/{orderId}/release-payment")
    public ResponseEntity<GigOrderResponseDTO> releasePayment(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(gigOrderService.releasePayment(orderId));
    }

    @PatchMapping("/{orderId}/refund")
    public ResponseEntity<GigOrderResponseDTO> refundBuyer(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(gigOrderService.refundBuyer(orderId));
    }

    @GetMapping
    public ResponseEntity<List<GigOrderResponseDTO>> getAll() {

        return ResponseEntity.ok(gigOrderService.getAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<GigOrderResponseDTO> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(gigOrderService.getById(id));
    }

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<GigOrderResponseDTO>> getBuyerOrders(
            @PathVariable Long buyerId) {

        return ResponseEntity.ok(gigOrderService.getBuyerOrders(buyerId));
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<GigOrderResponseDTO>> getSellerOrders(
            @PathVariable Long sellerId) {

        return ResponseEntity.ok(gigOrderService.getSellerOrders(sellerId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<GigOrderResponseDTO>> getByStatus(
            @PathVariable GigOrderStatus status) {

        return ResponseEntity.ok(gigOrderService.getByStatus(status));
    }

    @GetMapping("/buyer/{buyerId}/status/{status}")
    public ResponseEntity<List<GigOrderResponseDTO>> findByBuyerIdAndStatus(
            @PathVariable Long buyerId,
            @PathVariable GigOrderStatus status) {

        return ResponseEntity.ok(
                gigOrderService.findByBuyerIdAndStatus(buyerId, status)
        );
    }

    @GetMapping("/buyer/{buyerId}/status/{status}/count")
    public Long countByBuyerIdAndStatus(
            @PathVariable Long buyerId,
            @PathVariable GigOrderStatus status) {

        return gigOrderService.countByBuyerIdAndStatus(buyerId, status);
    }

    @GetMapping("/seller/{sellerId}/status/{status}")
    public ResponseEntity<List<GigOrderResponseDTO>> findByGigUserProfileIdAndStatus(
            @PathVariable Long sellerId,
            @PathVariable GigOrderStatus status) {

        return ResponseEntity.ok(
                gigOrderService.findByGigUserProfileIdAndStatus(sellerId, status)
        );
    }

    @GetMapping("/seller/{sellerId}/status/{status}/count")
    public Long countByGigUserProfileIdAndStatus(
            @PathVariable Long sellerId,
            @PathVariable GigOrderStatus status) {

        return gigOrderService.countByGigUserProfileIdAndStatus(sellerId, status);
    }


    @GetMapping("/gig/{gigId}")
    public ResponseEntity<List<GigOrderResponseDTO>> findByGigId(
            @PathVariable Long gigId) {

        return ResponseEntity.ok(
                gigOrderService.findByGigId(gigId)
        );
    }

    @GetMapping("/gig/{gigId}/count")
    public ResponseEntity<Long> countByGigId(
            @PathVariable Long gigId) {

        return ResponseEntity.ok(
                gigOrderService.countByGigId(gigId)
        );
    }

    @GetMapping("/seller/{sellerId}/count")
    public ResponseEntity<Long> countByGigUserProfileId(
            @PathVariable Long sellerId) {

        return ResponseEntity.ok(
                gigOrderService.countByGigUserProfileId(sellerId)
        );
    }

    @GetMapping("/buyer/{buyerId}/count")
    public ResponseEntity<Long> countByBuyerId(
            @PathVariable Long buyerId) {

        return ResponseEntity.ok(
                gigOrderService.countByBuyerId(buyerId)
        );
    }

    @GetMapping("/gig/{gigId}/buyer/{buyerId}/exist")
    public Boolean existsByGigIdAndBuyerId(@PathVariable Long gigId, @PathVariable Long buyerId) {
        return gigOrderService.existsByGigIdAndBuyerId(gigId, buyerId);
    }

    @GetMapping("/gig/{gigId}/buyer/{buyerId}/active")
    public ResponseEntity<GigOrderResponseDTO> findActiveOrder(
            @PathVariable Long gigId,
            @PathVariable Long buyerId
    ) {

        return ResponseEntity.ok(
                gigOrderService.findActiveOrder(
                        gigId,
                        buyerId
                )
        );

    }

    @PostMapping("search")
    public List<GigOrderResponseDTO> search(
            @RequestBody GigOrderFilterRequestDTO dto
    ) {

        return gigOrderService.search(dto);

    }


}