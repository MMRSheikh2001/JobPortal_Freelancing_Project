package com.wordbridge.project.sslcommerz;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "sslcommerz")
public class SSLCommerzConfig {
    private String storeId;

    private String storePassword;

    private String sessionUrl;

    private String validationUrl;

    private String successUrl;

    private String failUrl;

    private String cancelUrl;

}
