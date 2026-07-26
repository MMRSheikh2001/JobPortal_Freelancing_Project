package com.wordbridge.project.gigorder;

import com.wordbridge.project.conversation.Conversation;
import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.GigOrderStatus;
import com.wordbridge.project.gig.Gig;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "gigorders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GigOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal quotedPrice;
    private BigDecimal agreedPrice;
    private BigDecimal finalPrice;


    @Lob
    @Column(columnDefinition = "TEXT")
    private String deliveryMessage;

    private String deliveryFileUrl;

    private Boolean paymentLocked;


    @Enumerated(value = EnumType.STRING)
    private GigOrderStatus status;


    @CreationTimestamp
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


    @ManyToOne
    @JoinColumn(name = "gig_id")
    private Gig gig;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @OneToOne(mappedBy = "gigOrder")
    private Conversation conversation;

}
