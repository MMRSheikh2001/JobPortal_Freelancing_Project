package com.wordbridge.project.dto.requestdto;

import lombok.Data;


@Data
public class CompanyProfileRequestDTO {


    private Long userId;

    private String name;

    private String phone;

    private String companyEmail;


    private String companyDescription;
    private String companyWebsite;
    private String industry;

    private String foundedYear;

    private String tradeLicenseNumber;


    private Long locationId;

    private String locationDetails;
    private String locationPostCode;
    private Long locationPoliceStationId;


}
