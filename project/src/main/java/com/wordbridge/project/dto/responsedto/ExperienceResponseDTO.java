package com.wordbridge.project.dto.responsedto;

import com.wordbridge.project.enums.EmploymentType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ExperienceResponseDTO {
    private Long id;

    private String companyName;
    private String position;
    private String responsibilities;
    private String achievements;

    private LocalDate startDate;
    private LocalDate endDate;

    private EmploymentType employmentType;

    private Boolean currentlyWorking;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    private Long userProfileId;
    private Long userId;

    private String userName;
    private String userEmail;


}
