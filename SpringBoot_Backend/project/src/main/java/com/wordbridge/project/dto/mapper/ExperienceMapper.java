package com.wordbridge.project.dto.mapper;

import com.wordbridge.project.dto.requestdto.ExperienceRequestDTO;
import com.wordbridge.project.dto.responsedto.ExperienceResponseDTO;
import com.wordbridge.project.entity.Experience;
import com.wordbridge.project.entity.UserProfile;
import com.wordbridge.project.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ExperienceMapper {
    private final UserProfileRepository userProfileRepository;

    public ExperienceResponseDTO toDTO(Experience e) {
        ExperienceResponseDTO dto = new ExperienceResponseDTO();
        dto.setId(e.getId());
        dto.setCompanyName(e.getCompanyName());
        dto.setPosition(e.getPosition());
        dto.setResponsibilities(e.getResponsibilities());
        dto.setAchievements(e.getAchievements());

        dto.setStartDate(e.getStartDate());
        dto.setEndDate(e.getEndDate());

        dto.setEmploymentType(e.getEmploymentType());
        dto.setCurrentlyWorking(e.getCurrentlyWorking());


        dto.setCreatedAt(e.getCreatedAt());
        dto.setUpdatedAt(e.getUpdatedAt());

        dto.setUserProfileId(e.getUserProfile().getId());
        dto.setUserId(e.getUserProfile().getUser().getId());

        dto.setUserName(e.getUserProfile().getName());
        dto.setUserEmail(e.getUserProfile().getUser().getEmail());

        return dto;
    }

    public Experience toEntity(ExperienceRequestDTO dto) {
        Experience e = new Experience();

        e.setCompanyName(dto.getCompanyName());
        e.setPosition(dto.getPosition());
        e.setResponsibilities(dto.getResponsibilities());
        e.setAchievements(dto.getAchievements());

        e.setStartDate(dto.getStartDate());
        e.setEndDate(dto.getEndDate());

        e.setEmploymentType(dto.getEmploymentType());

        e.setCurrentlyWorking(
                dto.getEndDate() == null ||
                        dto.getEndDate().isAfter(LocalDate.now())
        );

        UserProfile userProfile = userProfileRepository.findById(dto.getUserProfileId())
                .orElseThrow(() -> new RuntimeException("User Profile Not Found"));

        e.setUserProfile(userProfile);

        return e;
    }


}
