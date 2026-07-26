package com.wordbridge.project.dto.requestdto;

import lombok.Data;

@Data
public class ExtracurricularRequestDTO {
    private String title;
    private String description;

    private String organization;
    private String role;

    private Long userProfileId;

}
