package com.wordbridge.project.gig;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GigSearchRequestDTO {

    private String keyword;

    private Long categoryId;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    private Integer maxDeliveryDays;

    private Boolean active;

    private Integer minimumRating;

    private Integer minimumOrders;


}
