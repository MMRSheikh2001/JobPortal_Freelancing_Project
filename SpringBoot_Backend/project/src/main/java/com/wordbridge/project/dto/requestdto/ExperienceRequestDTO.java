package com.wordbridge.project.dto.requestdto;

import com.wordbridge.project.enums.EmploymentType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ExperienceRequestDTO {

    private String companyName;
    private String position;
    private String responsibilities;
    private String achievements;

    private LocalDate startDate;
    private LocalDate endDate;

    private EmploymentType employmentType;

    private Long userProfileId;

}
