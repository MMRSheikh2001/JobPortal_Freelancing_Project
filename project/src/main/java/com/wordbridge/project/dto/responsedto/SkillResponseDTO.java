package com.wordbridge.project.dto.responsedto;


import lombok.Data;


@Data
public class SkillResponseDTO {
    private Long skillId;
    private String skillName;
    private Long categoryId;
    private String categoryName;
    private String categoryDescription;

}
