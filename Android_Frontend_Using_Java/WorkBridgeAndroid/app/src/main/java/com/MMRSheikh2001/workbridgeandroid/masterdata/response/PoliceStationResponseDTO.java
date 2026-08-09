package com.MMRSheikh2001.workbridgeandroid.masterdata.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PoliceStationResponseDTO {
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
