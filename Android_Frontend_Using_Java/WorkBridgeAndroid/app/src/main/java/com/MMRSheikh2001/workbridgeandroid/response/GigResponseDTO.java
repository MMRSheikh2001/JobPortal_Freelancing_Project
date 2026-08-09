package com.MMRSheikh2001.workbridgeandroid.response;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class GigResponseDTO {

    private Long id;

    private  String title;
    private String shortDescription;
    private String description;

    private BigDecimal startingPrice;
    private Integer deliveryDays;
    private Integer revisions;

    private String gigImage;

    private Boolean isActive;


    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;

    private Long categoryId;
    private String categoryName;


    private Long userProfileId;
    private String userName;

    private Double averageRating;

    private Integer totalReviews;

    private Integer completedOrders;



}