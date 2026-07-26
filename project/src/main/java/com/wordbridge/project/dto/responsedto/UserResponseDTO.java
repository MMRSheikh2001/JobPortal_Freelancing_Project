package com.wordbridge.project.dto.responsedto;

import com.wordbridge.project.enums.UserRole;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

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
