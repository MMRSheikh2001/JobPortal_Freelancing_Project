package com.wordbridge.project.job;

import lombok.Data;

@Data
public class CompanySearchRequestDTO {

    private String keyword;

    private String industry;

    private Long countryId;

    private Long divisionId;

    private Long districtId;

}
