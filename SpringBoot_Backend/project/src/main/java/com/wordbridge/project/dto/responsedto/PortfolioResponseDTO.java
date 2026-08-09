package com.wordbridge.project.dto.responsedto;

import lombok.Data;

import java.time.LocalDateTime;

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
