package com.wordbridge.project.resumeimport;

import com.wordbridge.project.dto.responsedto.*;
import lombok.Data;

import java.util.List;

@Data
public class ResumeImportPreviewDTO {

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
