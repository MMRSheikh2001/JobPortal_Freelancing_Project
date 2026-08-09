package com.MMRSheikh2001.workbridgeandroid.cvinformations.request;

import com.MMRSheikh2001.workbridgeandroid.enums.TrainingType;

import java.time.LocalDate;

import lombok.Data;

@Data
public class TrainingRequestDTO {

    private String name;
    private String description;

    private String institution;

    private LocalDate startDate;
    private LocalDate endDate;
    private String duration;


    private String certificateVerificationUrl;
    private String certificateId;

    private TrainingType trainingType;

    private Long userProfileId;

}