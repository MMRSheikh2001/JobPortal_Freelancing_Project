package com.wordbridge.project.dto.requestdto;

import lombok.Data;

@Data
public class ReferenceRequestDTO {
    private String name;

    private String organization;
    private String designation;

    private String phone;
    private String email;
    private String address;


    private String relation;

    private Long userProfileId;

}
