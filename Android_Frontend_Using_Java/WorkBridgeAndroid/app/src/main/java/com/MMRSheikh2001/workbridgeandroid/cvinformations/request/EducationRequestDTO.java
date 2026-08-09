package com.MMRSheikh2001.workbridgeandroid.cvinformations.request;


import com.MMRSheikh2001.workbridgeandroid.enums.EducationLevel;
import com.MMRSheikh2001.workbridgeandroid.enums.ResultType;

import lombok.Data;

@Data
public class EducationRequestDTO {


    private EducationLevel educationLevel;

    private String board;

    private String institution;

    private String fieldOfStudy;


    private ResultType resultType;
    private Double result;
    private Double outOf;
    private String gradeOrDivision;

    private String startDate;
    private String endDate;


    private Long userProfileId;


}
