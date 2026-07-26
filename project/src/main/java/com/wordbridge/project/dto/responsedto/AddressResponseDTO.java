package com.wordbridge.project.dto.responsedto;

import lombok.Data;

@Data
public class AddressResponseDTO {
    private Long addressId;
    private String addressDetails;
    private String postCode;

    private Long policeStationId;
    private String policeStationName;

    private Long districtId;
    private String districtName;

    private Long divisionId;
    private String divisionName;

    private Long countryId;
    private String countryName;
    private String countryCode;

}
