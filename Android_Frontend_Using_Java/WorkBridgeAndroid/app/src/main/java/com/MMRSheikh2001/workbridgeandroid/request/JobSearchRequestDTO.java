package com.MMRSheikh2001.workbridgeandroid.request;

import com.MMRSheikh2001.workbridgeandroid.enums.EmploymentType;
import com.MMRSheikh2001.workbridgeandroid.enums.WorkPlaceType;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class JobSearchRequestDTO {

    private String keyword;

    private Long categoryId;

    private Long countryId;

    private Long divisionId;

    private Long districtId;

    private Long policeStationId;

    private EmploymentType employmentType;

    private WorkPlaceType workPlaceType;

    private BigDecimal minSalary;

    private BigDecimal maxSalary;

    private Boolean active;


}
