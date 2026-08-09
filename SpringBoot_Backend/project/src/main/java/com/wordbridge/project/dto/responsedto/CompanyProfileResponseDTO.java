package com.wordbridge.project.dto.responsedto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CompanyProfileResponseDTO {
    private Long id;

    private Long userId;
    private String userEmail;

    private String name;

    private String phone;

    private String companyEmail;

    private String image;


    private String companyDescription;
    private String companyWebsite;
    private String industry;

    private String foundedYear;

    private String tradeLicenseNumber;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long locationId;
    private String locationDetails;
    private String locationPostCode;

    private Long locationCountryId;
    private String locationCountryName;
    private String locationCountryCode;

    private Long locationDivisionId;
    private String locationDivisionName;

    private Long locationDistrictId;
    private String locationDistrictName;

    private Long locationPoliceStationId;
    private String locationPoliceStationName;


}
