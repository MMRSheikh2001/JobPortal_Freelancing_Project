package com.MMRSheikh2001.workbridgeandroid.cvinformations.response;


import com.MMRSheikh2001.workbridgeandroid.enums.EducationLevel;
import com.MMRSheikh2001.workbridgeandroid.enums.ResultType;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class EducationResponseDTO {
    private Long id;


    private EducationLevel educationLevel;

    private String board;

    private String institution;

    private String fieldOfStudy;


    private ResultType resultType;
    private Double result;
    private Double outOf;
    private String gradeOrDivision;

    private LocalDate startDate;
    private LocalDate endDate;

    private Boolean currentlyStudying;


    private LocalDateTime createdAt;

    private Long userProfileId;
    private Long userId;
    private String userName;
    private String userEmail;


}