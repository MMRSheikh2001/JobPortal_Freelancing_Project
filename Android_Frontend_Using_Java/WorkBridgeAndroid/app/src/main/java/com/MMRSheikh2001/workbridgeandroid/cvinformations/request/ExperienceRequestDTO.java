package com.MMRSheikh2001.workbridgeandroid.cvinformations.request;

import com.MMRSheikh2001.workbridgeandroid.enums.EmploymentType;

import java.time.LocalDate;

import lombok.Data;

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
