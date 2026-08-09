package com.wordbridge.project.dto.responsedto;

import com.wordbridge.project.enums.GenderType;
import com.wordbridge.project.enums.JobType;
import com.wordbridge.project.enums.WorkPlaceType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserProfileResponseDTO {
    private Long id;

    private Long userId;
    private String userEmail;

    private String name;
    private String phone;
    private String image;

    private String headline;
    private String professionalSummary;
    private String bio;

    private LocalDate dateOfBirth;

    private GenderType gender;
    private String nationality;
    private String religion;
    private String maritalStatus;

    private String fatherName;
    private String motherName;

    private String nidNumber;
    private String passportNumber;

    private String githubLink;
    private String linkedinLink;
    private String portfolioWebsite;


    private BigDecimal expectedSalary;
    private BigDecimal currentSalary;


    private JobType preferredJobType;
    private WorkPlaceType preferredWorkplace;

    private String careerObjective;
    private String freelancerTitle;

    private Long presentAddressId;
    private String presentAddressDetails;
    private String presentAddressPostCode;

    private Long presentCountryId;
    private String presentCountryName;
    private String presentCountryCode;

    private Long presentDivisionId;
    private String presentDivisionName;

    private Long presentDistrictId;
    private String presentDistrictName;

    private Long presentPoliceStationId;
    private String presentPoliceStationName;

    private Long permanentAddressId;
    private String permanentAddressDetails;
    private String permanentAddressPostCode;


    private Long permanentCountryId;
    private String permanentCountryName;
    private String permanentCountryCode;

    private Long permanentDivisionId;
    private String permanentDivisionName;

    private Long permanentDistrictId;
    private String permanentDistrictName;

    private Long permanentPoliceStationId;
    private String permanentPoliceStationName;


    private Boolean profileCompleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
