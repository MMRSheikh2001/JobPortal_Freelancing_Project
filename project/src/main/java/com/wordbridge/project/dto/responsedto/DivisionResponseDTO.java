package com.wordbridge.project.dto.responsedto;

import lombok.Data;

@Data
public class DivisionResponseDTO {
    private Long divisionId;
    private String divisionName;

    private Long countryId;
    private String countryName;

    private String countryCode;
}
