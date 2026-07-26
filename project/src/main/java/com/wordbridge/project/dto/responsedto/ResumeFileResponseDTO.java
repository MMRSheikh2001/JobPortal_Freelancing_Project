package com.wordbridge.project.dto.responsedto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResumeFileResponseDTO {

    private Long id;
    private Long userProfileId;
    private String userName;

    private String fileName;
    private LocalDateTime uploadedAt;

}
