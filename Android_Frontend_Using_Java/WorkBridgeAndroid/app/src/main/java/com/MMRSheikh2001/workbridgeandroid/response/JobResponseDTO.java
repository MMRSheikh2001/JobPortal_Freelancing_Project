package com.MMRSheikh2001.workbridgeandroid.response;

import com.MMRSheikh2001.workbridgeandroid.enums.EmploymentType;
import com.MMRSheikh2001.workbridgeandroid.enums.WorkPlaceType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class JobResponseDTO {
    private Long id;

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
    private Boolean isActive;

    private Integer vacancy;


    private EmploymentType employmentType;


    private WorkPlaceType workPlaceType;


    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;

    private Long companyProfileId;
    private Long userId;
    private String userEmail;
    private String companyName;
    private String companyEmail;
    private String companyPhone;
    private String companyDescription;
    private String companyWebsite;
    private String companyLogo;

    private Long locationCountryId;
    private String locationCountryName;
    private String locationCountryCode;

    private Long locationDivisionId;
    private String locationDivisionName;

    private Long locationDistrictId;
    private String locationDistrictName;

    private Long locationPoliceStationId;
    private String locationPoliceStationName;

    private Long categoryId;
    private String categoryName;

    //AI Integration
    private Boolean aiScreeningEnabled;

    private Boolean aiCvScreeningEnabled;

    private Boolean aiInterviewEnabled;

    private Integer aiMatchThreshold;

    private Integer aiQuestionCount;

    private Integer aiShortlistCount;

    private Integer aiDeadlineDays;

}
