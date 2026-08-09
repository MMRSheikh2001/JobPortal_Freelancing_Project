package com.MMRSheikh2001.workbridgeandroid.cvinformations.response;

import com.MMRSheikh2001.workbridgeandroid.response.UserProfileResponseDTO;

import java.util.List;

import lombok.Data;

@Data
public class ResumeResponseDTO {
    private UserProfileResponseDTO profile;

    private List<EducationResponseDTO> educations;

    private List<ExperienceResponseDTO> experiences;

    private List<UserSkillResponseDTO> skills;

    private List<UserLanguageResponseDTO> languages;

    private List<TrainingResponseDTO> trainings;

    private List<PortfolioResponseDTO> portfolios;

    private List<ReferenceResponseDTO> references;

    private List<ExtracurricularResponseDTO> extracurriculars;


}

