package com.wordbridge.project.saved;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SavedJobResponseDTO {

    private Long id;
    private LocalDateTime createdAt;

    private Long userId;
    private String userName;

    private Long jobId;
    private String jobTitle;
    private String jobDescription;

    private String companyName;
    private String companyLogo;
}
