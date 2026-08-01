package com.wordbridge.project.resumeimport;

import com.wordbridge.project.dto.responsedto.*;
import com.wordbridge.project.entity.*;
import com.wordbridge.project.repository.*;
import com.wordbridge.project.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeImportServiceImpl implements ResumeImportService {

    private final ResumeTextExtractor resumeTextExtractor;
    private final ResumeRepository resumeRepository;
    private final ResumeService resumeService;


    private final UserProfileRepository userProfileRepository;
    private final EducationRepository educationRepository;
    private final ExperienceRepository experienceRepository;
    private final TrainingRepository trainingRepository;
    private final PortfolioRepository portfolioRepository;
    private final ReferenceRepository referenceRepository;
    private final ExtracurricularRepository extracurricularRepository;


    private final GeminiResumeParser geminiResumeParser;


    @Value("${image.upload.dir}")
    private String uploadDir;


    @Override
    public ResumeImportPreviewDTO getPreviewFromGemini(Long userProfileId) {
        ResumeResponseDTO resumeResponseDTO = resumeService.generateResume(userProfileId);

        Resume resume = resumeRepository.findByUserProfileId(userProfileId)
                .orElseThrow(() -> new RuntimeException("No resume found"));
        String resumeText = extractTextFromCV(resume.getId());

        ResumeImportPreviewDTO resumeImportPreviewDTO = geminiResumeParser.parseResume(resumeText, resumeResponseDTO);

        return resumeImportPreviewDTO;
    }

    @Override
    @Transactional
    public void saveImportedResume(
            Long userProfileId,
            ResumeImportPreviewDTO preview
    ) {

        UserProfile profile =
                userProfileRepository.findById(userProfileId)
                        .orElseThrow(() ->
                                new RuntimeException("User Profile not found"));

        if (preview == null) {
            return;
        }

        saveProfile(profile, preview.getProfile());

        saveEducations(profile, preview.getEducations());

        saveExperiences(profile, preview.getExperiences());

        saveTrainings(profile, preview.getTrainings());

        savePortfolios(profile, preview.getPortfolios());

        saveReferences(profile, preview.getReferences());

        saveExtracurriculars(profile, preview.getExtracurriculars());

    }


    //For Saving

    //User Profile save
    private void saveProfile(
            UserProfile entity,
            UserProfileResponseDTO dto
    ) {

        if (dto == null) {
            return;
        }

        if (dto.getName() != null)
            entity.setName(dto.getName());

        if (dto.getPhone() != null)
            entity.setPhone(dto.getPhone());

        if (dto.getHeadline() != null)
            entity.setHeadline(dto.getHeadline());

        if (dto.getProfessionalSummary() != null)
            entity.setProfessionalSummary(dto.getProfessionalSummary());

        if (dto.getBio() != null)
            entity.setBio(dto.getBio());

        if (dto.getDateOfBirth() != null)
            entity.setDateOfBirth(dto.getDateOfBirth());

        if (dto.getGender() != null)
            entity.setGender(dto.getGender());

        if (dto.getNationality() != null)
            entity.setNationality(dto.getNationality());

        if (dto.getReligion() != null)
            entity.setReligion(dto.getReligion());

        if (dto.getMaritalStatus() != null)
            entity.setMaritalStatus(dto.getMaritalStatus());

        if (dto.getFatherName() != null)
            entity.setFatherName(dto.getFatherName());

        if (dto.getMotherName() != null)
            entity.setMotherName(dto.getMotherName());

        if (dto.getNidNumber() != null)
            entity.setNidNumber(dto.getNidNumber());

        if (dto.getPassportNumber() != null)
            entity.setPassportNumber(dto.getPassportNumber());

        if (dto.getGithubLink() != null)
            entity.setGithubLink(dto.getGithubLink());

        if (dto.getLinkedinLink() != null)
            entity.setLinkedinLink(dto.getLinkedinLink());

        if (dto.getPortfolioWebsite() != null)
            entity.setPortfolioWebsite(dto.getPortfolioWebsite());

        if (dto.getExpectedSalary() != null)
            entity.setExpectedSalary(dto.getExpectedSalary());

        if (dto.getCurrentSalary() != null)
            entity.setCurrentSalary(dto.getCurrentSalary());

        if (dto.getPreferredJobType() != null)
            entity.setPreferredJobType(dto.getPreferredJobType());

        if (dto.getPreferredWorkplace() != null)
            entity.setPreferredWorkplace(dto.getPreferredWorkplace());

        if (dto.getCareerObjective() != null)
            entity.setCareerObjective(dto.getCareerObjective());

        if (dto.getFreelancerTitle() != null)
            entity.setFreelancerTitle(dto.getFreelancerTitle());

        userProfileRepository.save(entity);

    }

    //Education save
    private void saveEducations(
            UserProfile userProfile,
            List<EducationResponseDTO> educations
    ) {

        if (educations == null || educations.isEmpty()) {
            return;
        }

        for (EducationResponseDTO dto : educations) {

            if (dto == null) {
                continue;
            }

            if (existsEducation(userProfile, dto)) {
                continue;
            }

            Education education = new Education();

            education.setUserProfile(userProfile);

            education.setEducationLevel(dto.getEducationLevel());

            education.setBoard(dto.getBoard());

            education.setInstitution(dto.getInstitution());

            education.setFieldOfStudy(dto.getFieldOfStudy());

            education.setResultType(dto.getResultType());

            education.setResult(dto.getResult());

            education.setOutOf(dto.getOutOf());

            education.setGradeOrDivision(dto.getGradeOrDivision());

            education.setStartDate(dto.getStartDate());

            education.setEndDate(dto.getEndDate());

            education.setCurrentlyStudying(dto.getCurrentlyStudying());

            educationRepository.save(education);

        }

    }

    private boolean existsEducation(
            UserProfile userProfile,
            EducationResponseDTO dto
    ) {

        return educationRepository.existsByUserProfileIdAndEducationLevelAndInstitutionIgnoreCaseAndFieldOfStudyIgnoreCase(
                userProfile.getId(),
                dto.getEducationLevel(),
                dto.getInstitution(),
                dto.getFieldOfStudy()
        );

    }


    //Expeience save
    private void saveExperiences(
            UserProfile userProfile,
            List<ExperienceResponseDTO> experiences
    ) {

        if (experiences == null || experiences.isEmpty()) {
            return;
        }

        for (ExperienceResponseDTO dto : experiences) {

            if (dto == null) {
                continue;
            }

            if (existsExperience(userProfile, dto)) {
                continue;
            }

            Experience experience = new Experience();

            experience.setUserProfile(userProfile);

            experience.setCompanyName(dto.getCompanyName());

            experience.setPosition(dto.getPosition());

            experience.setResponsibilities(dto.getResponsibilities());

            experience.setAchievements(dto.getAchievements());

            experience.setStartDate(dto.getStartDate());

            experience.setEndDate(dto.getEndDate());

            experience.setEmploymentType(dto.getEmploymentType());

            experience.setCurrentlyWorking(dto.getCurrentlyWorking());

            experienceRepository.save(experience);

        }

    }

    private boolean existsExperience(
            UserProfile userProfile,
            ExperienceResponseDTO dto
    ) {

        return experienceRepository
                .existsByUserProfileIdAndCompanyNameIgnoreCaseAndPositionIgnoreCaseAndStartDate(
                        userProfile.getId(),
                        dto.getCompanyName(),
                        dto.getPosition(),
                        dto.getStartDate()
                );

    }


    //Save trainings

    private void saveTrainings(
            UserProfile userProfile,
            List<TrainingResponseDTO> trainings
    ) {

        if (trainings == null || trainings.isEmpty()) {
            return;
        }

        for (TrainingResponseDTO dto : trainings) {

            if (dto == null) {
                continue;
            }

            if (existsTraining(userProfile, dto)) {
                continue;
            }

            Training training = new Training();

            training.setUserProfile(userProfile);

            training.setName(dto.getName());

            training.setDescription(dto.getDescription());

            training.setInstitution(dto.getInstitution());

            training.setStartDate(dto.getStartDate());

            training.setEndDate(dto.getEndDate());

            training.setCompleted(dto.getCompleted());

            training.setDuration(dto.getDuration());

            // Ignore certificate file during AI import
            training.setCertificateFile(null);

            training.setCertificateVerificationUrl(dto.getCertificateVerificationUrl());

            training.setCertificateId(dto.getCertificateId());

            training.setTrainingType(dto.getTrainingType());

            trainingRepository.save(training);

        }

    }


    private boolean existsTraining(
            UserProfile userProfile,
            TrainingResponseDTO dto
    ) {

        return trainingRepository
                .existsByUserProfileIdAndNameIgnoreCaseAndInstitutionIgnoreCase(
                        userProfile.getId(),
                        dto.getName(),
                        dto.getInstitution()
                );

    }

    //save portfolio
    private void savePortfolios(
            UserProfile userProfile,
            List<PortfolioResponseDTO> portfolios
    ) {

        if (portfolios == null || portfolios.isEmpty()) {
            return;
        }

        for (PortfolioResponseDTO dto : portfolios) {

            if (dto == null) {
                continue;
            }

            if (existsPortfolio(userProfile, dto)) {
                continue;
            }

            Portfolio portfolio = new Portfolio();

            portfolio.setUserProfile(userProfile);

            portfolio.setTitle(dto.getTitle());

            portfolio.setDescription(dto.getDescription());

            portfolio.setProjectUrl(dto.getProjectUrl());

            // Ignore uploaded attachment during AI import
            portfolio.setFileUrl(null);

            portfolio.setTechnologies(dto.getTechnologies());

            portfolioRepository.save(portfolio);

        }

    }

    private boolean existsPortfolio(
            UserProfile userProfile,
            PortfolioResponseDTO dto
    ) {

        return portfolioRepository
                .existsByUserProfileIdAndTitleIgnoreCase(
                        userProfile.getId(),
                        dto.getTitle()
                );

    }

    // save reference
    private void saveReferences(
            UserProfile userProfile,
            List<ReferenceResponseDTO> references
    ) {

        if (references == null || references.isEmpty()) {
            return;
        }

        for (ReferenceResponseDTO dto : references) {

            if (dto == null) {
                continue;
            }

            if (existsReference(userProfile, dto)) {
                continue;
            }

            Reference reference = new Reference();

            reference.setUserProfile(userProfile);

            reference.setName(dto.getName());

            reference.setOrganization(dto.getOrganization());

            reference.setDesignation(dto.getDesignation());

            reference.setPhone(dto.getPhone());

            reference.setEmail(dto.getEmail());

            reference.setAddress(dto.getAddress());

            reference.setRelation(dto.getRelation());

            referenceRepository.save(reference);

        }

    }

    private boolean existsReference(
            UserProfile userProfile,
            ReferenceResponseDTO dto
    ) {

        return referenceRepository
                .existsByUserProfileIdAndEmailIgnoreCase(
                        userProfile.getId(),
                        dto.getEmail()
                );

    }

    //Extracurricular save
    private void saveExtracurriculars(
            UserProfile userProfile,
            List<ExtracurricularResponseDTO> extracurriculars
    ) {

        if (extracurriculars == null || extracurriculars.isEmpty()) {
            return;
        }

        for (ExtracurricularResponseDTO dto : extracurriculars) {

            if (dto == null) {
                continue;
            }

            if (existsExtracurricular(userProfile, dto)) {
                continue;
            }

            Extracurricular extracurricular = new Extracurricular();

            extracurricular.setUserProfile(userProfile);

            extracurricular.setTitle(dto.getTitle());

            extracurricular.setDescription(dto.getDescription());

            extracurricular.setOrganization(dto.getOrganization());

            extracurricular.setRole(dto.getRole());

            extracurricularRepository.save(extracurricular);

        }

    }

    private boolean existsExtracurricular(
            UserProfile userProfile,
            ExtracurricularResponseDTO dto
    ) {

        return extracurricularRepository
                .existsByUserProfileIdAndTitleIgnoreCaseAndOrganizationIgnoreCase(
                        userProfile.getId(),
                        dto.getTitle(),
                        dto.getOrganization()
                );

    }


    //Gemini methods
    @Override
    public String extractTextFromCV(Long resumeFileId) {
        Resume resume = resumeRepository.findById(resumeFileId)
                .orElseThrow(() -> new RuntimeException("No Resume found"));

        Path path = Paths.get(uploadDir, "resumes", resume.getFileName());

        String extractedText = resumeTextExtractor.extractText(path);

        return extractedText;


    }


}
