package com.MMRSheikh2001.workbridgeandroid.masterdata.response;

import lombok.Data;

@Data
public class DivisionResponseDTO {
    private Long divisionId;
    private String divisionName;

    private Long countryId;
    private String countryName;

    private String countryCode;
}
