package com.wordbridge.project.saved;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SavedGigResponseDTO {

    private Long id;
    private LocalDateTime createdAt;

    private Long userId;
    private String userName;

    private Long gigId;
    private String gigTitle;
    private String gigDescription;
    private String gigImage;

    private String freelancerName;



}
