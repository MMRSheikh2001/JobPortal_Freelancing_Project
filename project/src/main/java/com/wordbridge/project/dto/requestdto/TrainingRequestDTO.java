package com.wordbridge.project.dto.requestdto;

import com.wordbridge.project.enums.TrainingType;
import lombok.Data;

import java.time.LocalDate;

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
