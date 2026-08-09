package com.wordbridge.project.dto.responsedto;

import lombok.Data;

@Data
public class CountryResponseDTO {
    private Long countryId;
    private String countryName;
    private String countryCode;
}
