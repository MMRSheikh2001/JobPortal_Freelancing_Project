package com.MMRSheikh2001.workbridgeandroid.cvinformations.response;

import com.MMRSheikh2001.workbridgeandroid.enums.TrainingType;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TrainingResponseDTO {

    private Long id;

    private String name;
    private String description;

    private String institution;

    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean completed;
    private String duration;

    private String certificateFile;
    private String certificateVerificationUrl;
    private String certificateId;

    private TrainingType trainingType;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long userProfileId;
    private String userName;

    private Long userId;
    private String userEmail;

}
