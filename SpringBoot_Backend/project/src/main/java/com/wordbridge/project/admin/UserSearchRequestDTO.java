package com.wordbridge.project.admin;

import com.wordbridge.project.enums.UserRole;
import lombok.Data;

@Data
public class UserSearchRequestDTO {

    private String keyword;

    private UserRole role;

    private Boolean isVerified;

    private Boolean isActive;

    private Boolean isSuspended;

}
