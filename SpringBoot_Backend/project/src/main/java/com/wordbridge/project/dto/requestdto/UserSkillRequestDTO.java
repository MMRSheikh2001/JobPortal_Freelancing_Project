package com.wordbridge.project.dto.requestdto;

import com.wordbridge.project.enums.ProficiencyLevel;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserSkillRequestDTO {


    private ProficiencyLevel proficiencyLevel;
    private Integer yearsOfExperience;


    private Long userProfileId;
    private Long skillId;


}
