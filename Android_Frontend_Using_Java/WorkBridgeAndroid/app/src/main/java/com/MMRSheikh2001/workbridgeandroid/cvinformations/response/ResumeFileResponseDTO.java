package com.MMRSheikh2001.workbridgeandroid.cvinformations.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ResumeFileResponseDTO {

    private Long id;
    private Long userProfileId;
    private String userName;

    private String fileName;
    private LocalDateTime uploadedAt;

}
