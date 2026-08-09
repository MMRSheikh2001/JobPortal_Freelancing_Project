package com.wordbridge.project.gigorder;

import com.wordbridge.project.enums.GigOrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class GigOrderRequestDTO {

    private BigDecimal quotedPrice;



    private String deliveryMessage;
    private String deliveryFileUrl;

    private Long gigId;




}
