package com.MMRSheikh2001.workbridgeandroid.masterdata.response;

import lombok.Data;

@Data
public class DistrictResponseDTO {
    private Long districtId;
    private String districtName;
    private Long divisionId;
    private String divisionName;
    private Long countryId;
    private String countryName;
    private String countryCode;
}
