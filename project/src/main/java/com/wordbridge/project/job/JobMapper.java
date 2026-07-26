package com.wordbridge.project.job;

import com.wordbridge.project.entity.Category;
import com.wordbridge.project.entity.CompanyProfile;
import com.wordbridge.project.entity.PoliceStation;
import com.wordbridge.project.repository.CategoryRepository;
import com.wordbridge.project.repository.CompanyProfileRepository;
import com.wordbridge.project.repository.PoliceStationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class JobMapper {
    private final CompanyProfileRepository companyProfileRepository;
    private final PoliceStationRepository policeStationRepository;
    private final CategoryRepository categoryRepository;

    public JobResponseDTO toDTO(Job j) {
        JobResponseDTO dto = new JobResponseDTO();

        dto.setId(j.getId());
        dto.setTitle(j.getTitle());
        dto.setJobDescription(j.getJobDescription());
        dto.setJobResponsibilities(j.getJobResponsibilities());
        dto.setEducationalRequirements(j.getEducationalRequirements());
        dto.setExperienceRequirements(j.getExperienceRequirements());
        dto.setMinExperience(j.getMinExperience());
        dto.setMaxExperience(j.getMaxExperience());
        dto.setAdditionalRequirements(j.getAdditionalRequirements());
        dto.setBenefits(j.getBenefits());
        dto.setSalaryMin(j.getSalaryMin());
        dto.setSalaryMax(j.getSalaryMax());
        dto.setIsNegotiable(j.getIsNegotiable());

        dto.setApplicationDeadline(j.getApplicationDeadline());
        if (dto.getApplicationDeadline() != null &&
                LocalDate.now().isAfter(dto.getApplicationDeadline())) {
            j.setIsActive(false);
        }

        dto.setIsActive(j.getIsActive());

        dto.setVacancy(j.getVacancy());
        dto.setEmploymentType(j.getEmploymentType());
        dto.setWorkPlaceType(j.getWorkPlaceType());
        dto.setCreatedAt(j.getCreatedAt());
        dto.setUpdatedAt(j.getUpdatedAt());


        dto.setCompanyProfileId(j.getCompanyProfile().getId());
        dto.setUserId(j.getCompanyProfile().getUser().getId());
        dto.setUserEmail(j.getCompanyProfile().getUser().getEmail());

        dto.setCompanyName(j.getCompanyProfile().getName());
        dto.setCompanyEmail(j.getCompanyProfile().getCompanyEmail());
        dto.setCompanyPhone(j.getCompanyProfile().getPhone());
        dto.setCompanyDescription(j.getCompanyProfile().getCompanyDescription());
        dto.setCompanyWebsite(j.getCompanyProfile().getCompanyWebsite());
        dto.setCompanyLogo(j.getCompanyProfile().getImage());

        dto.setLocationCountryId(j.getJobLocation().getDistrict().getDivision().getCountry().getId());
        dto.setLocationCountryName(j.getJobLocation().getDistrict().getDivision().getCountry().getName());
        dto.setLocationCountryCode(j.getJobLocation().getDistrict().getDivision().getCountry().getCode());

        dto.setLocationDivisionId(j.getJobLocation().getDistrict().getDivision().getId());
        dto.setLocationDivisionName(j.getJobLocation().getDistrict().getDivision().getName());

        dto.setLocationDistrictId(j.getJobLocation().getDistrict().getId());
        dto.setLocationDistrictName(j.getJobLocation().getDistrict().getName());

        dto.setLocationPoliceStationId(j.getJobLocation().getId());
        dto.setLocationPoliceStationName(j.getJobLocation().getName());

        dto.setCategoryId(j.getCategory().getId());
        dto.setCategoryName(j.getCategory().getName());

        dto.setAiScreeningEnabled(j.getAiScreeningEnabled());
        dto.setAiCvScreeningEnabled(j.getAiCvScreeningEnabled());
        dto.setAiInterviewEnabled(j.getAiInterviewEnabled());
        dto.setAiMatchThreshold(j.getAiMatchThreshold());
        dto.setAiQuestionCount(j.getAiQuestionCount());
        dto.setAiShortlistCount(j.getAiShortlistCount());
        dto.setAiDeadlineDays(j.getAiDeadlineDays());


        return dto;
    }

    public Job toEntity(JobRequestDTO dto) {
        Job j = new Job();

        j.setTitle(dto.getTitle());
        j.setJobDescription(dto.getJobDescription());
        j.setJobResponsibilities(dto.getJobResponsibilities());
        j.setEducationalRequirements(dto.getEducationalRequirements());
        j.setExperienceRequirements(dto.getExperienceRequirements());
        j.setMinExperience(dto.getMinExperience());
        j.setMaxExperience(dto.getMaxExperience());
        j.setAdditionalRequirements(dto.getAdditionalRequirements());
        j.setBenefits(dto.getBenefits());
        j.setSalaryMin(dto.getSalaryMin());
        j.setSalaryMax(dto.getSalaryMax());
        j.setIsNegotiable(dto.getIsNegotiable());

        j.setApplicationDeadline(dto.getApplicationDeadline());
        j.setIsActive(true);

        if (dto.getApplicationDeadline() != null &&
                LocalDate.now().isAfter(dto.getApplicationDeadline())) {
            j.setIsActive(false);
        }


        j.setVacancy(dto.getVacancy());
        j.setEmploymentType(dto.getEmploymentType());
        j.setWorkPlaceType(dto.getWorkPlaceType());

        CompanyProfile companyProfile = companyProfileRepository.findById(dto.getCompanyProfileId())
                .orElseThrow(() -> new RuntimeException("No Company Profile found"));
        j.setCompanyProfile(companyProfile);

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("No category found"));
        j.setCategory(category);

        PoliceStation policeStation = policeStationRepository.findById(dto.getLocationPoliceStationId())
                .orElseThrow(() -> new RuntimeException("No Police station found"));
        j.setJobLocation(policeStation);

        j.setAiScreeningEnabled(dto.getAiScreeningEnabled());
        j.setAiCvScreeningEnabled(dto.getAiCvScreeningEnabled());
        j.setAiInterviewEnabled(dto.getAiInterviewEnabled());
        j.setAiMatchThreshold(dto.getAiMatchThreshold());
        j.setAiQuestionCount(dto.getAiQuestionCount());
        j.setAiShortlistCount(dto.getAiShortlistCount());
        j.setAiDeadlineDays(dto.getAiDeadlineDays());


        return j;
    }


}
