package com.MMRSheikh2001.workbridgeandroid.cvinformations.request;

import com.MMRSheikh2001.workbridgeandroid.enums.ProficiencyLevel;

import lombok.Data;

@Data
public class UserSkillRequestDTO {


    private ProficiencyLevel proficiencyLevel;
    private Integer yearsOfExperience;


    private Long userProfileId;
    private Long skillId;


}