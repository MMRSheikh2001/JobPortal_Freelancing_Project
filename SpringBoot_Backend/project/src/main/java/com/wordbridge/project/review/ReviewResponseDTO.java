package com.wordbridge.project.review;

import lombok.Data;


import java.time.LocalDateTime;

@Data
public class ReviewResponseDTO {

    private Long id;

    private Integer rating;
    private String comment;


    private LocalDateTime createdAt;

    private Long gigOrderId;

    private Long reviewerId;
    private String reviewerName;

    private Long sellerUserProfileId;
    private String sellerName;

    private Long gigId;
    private String gigTitle;


}
