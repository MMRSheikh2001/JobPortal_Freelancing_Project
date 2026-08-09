package com.MMRSheikh2001.workbridgeandroid.cvinformations.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PortfolioResponseDTO {

    private Long id;

    private String title;
    private String description;

    private String projectUrl;


    private String fileUrl;

    private String technologies;


    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;

    private Long userProfileId;
    private String userName;

    private Long userId;
    private String userEmail;


}