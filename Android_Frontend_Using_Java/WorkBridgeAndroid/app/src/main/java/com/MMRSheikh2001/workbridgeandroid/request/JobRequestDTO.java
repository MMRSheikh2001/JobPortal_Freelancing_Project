package com.MMRSheikh2001.workbridgeandroid.request;

import com.MMRSheikh2001.workbridgeandroid.enums.EmploymentType;
import com.MMRSheikh2001.workbridgeandroid.enums.WorkPlaceType;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class JobRequestDTO {

    private String title;
    private String jobDescription;

    private String jobResponsibilities;
    private String educationalRequirements;

    private String experienceRequirements;
    private Integer minExperience;
    private Integer maxExperience;

    private String additionalRequirements;
    private String benefits;

    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private Boolean isNegotiable;


    private LocalDate applicationDeadline;


    private Integer vacancy;


    private EmploymentType employmentType;


    private WorkPlaceType workPlaceType;


    private Long companyProfileId;


    private Long locationPoliceStationId;

    private Long categoryId;

    //AI Integration
    private Boolean aiScreeningEnabled;

    private Boolean aiCvScreeningEnabled;

    private Boolean aiInterviewEnabled;

    private Integer aiMatchThreshold;

    private Integer aiQuestionCount;

    private Integer aiShortlistCount;

    private Integer aiDeadlineDays;


}
