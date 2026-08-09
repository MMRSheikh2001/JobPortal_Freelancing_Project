package com.wordbridge.project.sslcommerz.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SSLSessionResponseDTO {
    private String status;

    private String failedreason;


    @JsonProperty("GatewayPageURL")
    private String GatewayPageURL;


}
