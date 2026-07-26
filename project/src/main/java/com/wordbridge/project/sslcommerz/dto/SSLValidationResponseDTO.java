package com.wordbridge.project.sslcommerz.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SSLValidationResponseDTO {
    // VALID / INVALID / FAILED
    private String status;

    // Your transaction ID
    @JsonProperty("tran_id")
    private String transactionId;

    // SSLCommerz validation ID
    @JsonProperty("val_id")
    private String validationId;

    // Amount paid
    private String amount;

    // Currency
    private String currency;

    // Payment method
    @JsonProperty("card_type")
    private String paymentMethod;

    // SSLCommerz transaction ID
    @JsonProperty("bank_tran_id")
    private String bankTransactionId;

    // Validation status
    @JsonProperty("APIConnect")
    private String apiConnect;

    // Risk level
    @JsonProperty("risk_level")
    private String riskLevel;

    @JsonProperty("risk_title")
    private String riskTitle;

}
