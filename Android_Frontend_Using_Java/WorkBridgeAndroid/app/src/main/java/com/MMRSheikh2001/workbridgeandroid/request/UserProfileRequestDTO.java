package com.MMRSheikh2001.workbridgeandroid.request;


import com.MMRSheikh2001.workbridgeandroid.enums.GenderType;
import com.MMRSheikh2001.workbridgeandroid.enums.JobType;
import com.MMRSheikh2001.workbridgeandroid.enums.WorkPlaceType;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class UserProfileRequestDTO {
    private Long userId;
    private String name;
    private String phone;


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
    private Long presentAddressPoliceStationId;

    private Long permanentAddressId;
    private String permanentAddressDetails;
    private String permanentAddressPostCode;
    private Long permanentAddressPoliceStationId;


}
