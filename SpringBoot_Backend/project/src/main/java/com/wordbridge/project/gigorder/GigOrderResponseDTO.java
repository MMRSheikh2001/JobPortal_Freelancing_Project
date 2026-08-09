package com.wordbridge.project.gigorder;

import com.wordbridge.project.enums.GigOrderStatus;
import com.wordbridge.project.enums.UserRole;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class GigOrderResponseDTO {

    private Long id;

    private BigDecimal quotedPrice;
    private BigDecimal agreedPrice;
    private BigDecimal finalPrice;

    private String deliveryMessage;
    private String deliveryFileUrl;

    private Boolean paymentLocked;



    private GigOrderStatus status;



    private LocalDateTime createdAt;

    // Seller sent quotation
    private LocalDateTime quotedAt;

    // Buyer accepted quotation
    private LocalDateTime quoteAcceptedAt;

    // Expected delivery deadline
    private LocalDateTime expectedDeliveryAt;

    // Seller delivered
    private LocalDateTime deliveredAt;

    // Buyer actions
    private LocalDateTime buyerAcceptedAt;
    private LocalDateTime buyerRejectedAt;
    private LocalDateTime buyerCancelledAt;

    // Seller actions
    private LocalDateTime sellerCancelledAt;
    private LocalDateTime sellerDisputeOpenedAt;

    // Seller has until this time to dispute
    private LocalDateTime sellerDisputeDeadline;

    // Final financial result
    private LocalDateTime paymentReleasedAt;
    private LocalDateTime refundedAt;

    private Long gigId;
    private String gigTitle;

    private String gigImage;

    private Long sellerId;
    private String sellerName;

    private Long buyerId;
    private String buyerName;

    private Long buyerUserProfileId;

    private Long buyerCompanyProfileId;

    private UserRole buyerRole;

    private  Long conversationId;


}
