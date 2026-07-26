package com.wordbridge.project.dto.responsedto;

import lombok.Data;

@Data
public class ExtracurricularResponseDTO {

    private Long id;

    private String title;
    private String description;

    private String organization;
    private String role;

    private Long userProfileId;
    private Long userId;
    private String userName;
    private String userEmail;


}
