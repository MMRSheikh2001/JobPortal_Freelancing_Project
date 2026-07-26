package com.wordbridge.project.payment;

import com.wordbridge.project.enums.UserRole;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResponseDTO {
    private Long id;

    private PaymentStatus paymentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private BigDecimal amount;

    private Long userId;
    private String userName;
    private UserRole userRole;

    private String gatewayTransactionId;

    private String validationId;

    private String paymentMethod;

    private String gateway;

    private String failureReason;


}
