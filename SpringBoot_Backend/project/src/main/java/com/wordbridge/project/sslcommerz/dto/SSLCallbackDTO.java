package com.wordbridge.project.sslcommerz.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SSLCallbackDTO {
    // Your transaction ID
    @JsonProperty("tran_id")
    private String transactionId;

    // SSLCommerz validation ID
    @JsonProperty("val_id")
    private String validationId;

    // VALID / FAILED / CANCELLED
    private String status;

    // Amount paid
    private String amount;

    // Payment method (BKASH, NAGAD, VISA, etc.)
    @JsonProperty("card_type")
    private String paymentMethod;

    // SSLCommerz transaction ID
    @JsonProperty("bank_tran_id")
    private String bankTransactionId;

    // Currency
    private String currency;

    // Failure reason (only on failure)
    @JsonProperty("failedreason")
    private String failedReason;

}
