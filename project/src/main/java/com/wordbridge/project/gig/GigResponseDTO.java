package com.wordbridge.project.gig;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
