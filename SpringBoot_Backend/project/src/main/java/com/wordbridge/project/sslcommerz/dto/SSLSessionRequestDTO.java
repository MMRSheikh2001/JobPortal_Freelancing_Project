package com.wordbridge.project.sslcommerz.dto;

import lombok.Data;

@Data
public class SSLSessionRequestDTO {
    // Merchant
    private String store_id;
    private String store_passwd;

    // Payment
    private String total_amount;
    private String currency;
    private String tran_id;

    // Redirect URLs
    private String success_url;
    private String fail_url;
    private String cancel_url;

    // Product
    private String product_name;
    private String product_category;
    private String product_profile;

    // Customer
    private String cus_name;
    private String cus_email;
    private String cus_phone;

}
