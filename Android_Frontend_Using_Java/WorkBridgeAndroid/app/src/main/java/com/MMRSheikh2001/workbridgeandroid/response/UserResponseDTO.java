package com.MMRSheikh2001.workbridgeandroid.response;

import com.MMRSheikh2001.workbridgeandroid.enums.UserRole;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserResponseDTO {

    private Long id;

    private String email;
    private String name;

    private UserRole role;



    private Boolean isVerified;

    private Boolean isActive;

    private Boolean isSuspended;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
