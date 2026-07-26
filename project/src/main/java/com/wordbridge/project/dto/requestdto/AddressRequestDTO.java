package com.wordbridge.project.dto.requestdto;

import lombok.Data;

@Data
public class AddressRequestDTO {

    private String addressDetails;
    private String postCode;
    private Long policeStationId;
}
