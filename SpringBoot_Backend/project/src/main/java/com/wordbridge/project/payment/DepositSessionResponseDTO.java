package com.wordbridge.project.payment;

import lombok.Data;

@Data
public class DepositSessionResponseDTO {
    private Long paymentId;

    private String gatewayTransactionId;

    private String gatewayPageUrl;

    private PaymentStatus paymentStatus;

}
