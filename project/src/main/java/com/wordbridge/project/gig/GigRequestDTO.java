package com.wordbridge.project.gig;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GigRequestDTO {

    private String title;
    private String shortDescription;
    private String description;

    private BigDecimal startingPrice;
    private Integer deliveryDays;
    private Integer revisions;

    private Long categoryId;


    private Long userProfileId;


}
