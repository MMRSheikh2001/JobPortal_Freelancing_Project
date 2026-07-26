package com.wordbridge.project.job;

import com.wordbridge.project.enums.EmploymentType;
import com.wordbridge.project.enums.WorkPlaceType;
import lombok.Data;

import java.math.BigDecimal;

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
