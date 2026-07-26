package com.wordbridge.project.dto.responsedto;

import lombok.Data;

@Data
public class ReferenceResponseDTO {
    private Long id;
    private String name;

    private String organization;
    private String designation;

    private String phone;
    private String email;
    private String address;


    private String relation;

    private Long userProfileId;
    private String userName;

    private Long userId;
    private String userEmail;

}
