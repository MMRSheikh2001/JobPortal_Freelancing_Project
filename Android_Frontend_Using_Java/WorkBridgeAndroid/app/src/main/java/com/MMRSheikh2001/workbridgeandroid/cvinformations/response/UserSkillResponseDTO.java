package com.MMRSheikh2001.workbridgeandroid.cvinformations.response;

import com.MMRSheikh2001.workbridgeandroid.enums.ProficiencyLevel;

import java.time.LocalDateTime;

import lombok.Data;

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
