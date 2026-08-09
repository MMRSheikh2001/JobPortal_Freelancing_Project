package com.wordbridge.project.dto.responsedto;

import com.wordbridge.project.enums.ProficiencyLevel;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class UserSkillResponseDTO {

    private Long id;

    private ProficiencyLevel proficiencyLevel;
    private Integer yearsOfExperience;
    private LocalDateTime createdAt;


    private Long userProfileId;
    private String userFullName;
    private String userHeadline;

    private Long userId;
    private String userEmail;

    private Long skillId;

    private String skillName;

    private Long categoryId;

    private String categoryName;

    private String categoryDescription;


}
