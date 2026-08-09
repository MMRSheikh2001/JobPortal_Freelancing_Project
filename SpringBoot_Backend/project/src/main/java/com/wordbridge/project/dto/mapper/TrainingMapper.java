package com.wordbridge.project.dto.mapper;

import com.wordbridge.project.dto.requestdto.TrainingRequestDTO;
import com.wordbridge.project.dto.responsedto.TrainingResponseDTO;
import com.wordbridge.project.entity.Training;
import com.wordbridge.project.entity.UserProfile;
import com.wordbridge.project.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class TrainingMapper {
    private final UserProfileRepository userProfileRepository;

    public TrainingResponseDTO toDTO(Training t) {
        TrainingResponseDTO dto = new TrainingResponseDTO();
        dto.setId(t.getId());
        dto.setName(t.getName());
        dto.setDescription(t.getDescription());

        dto.setInstitution(t.getInstitution());

        dto.setStartDate(t.getStartDate());
        dto.setEndDate(t.getEndDate());
        dto.setCompleted(t.getCompleted());
        dto.setDuration(t.getDuration());

        dto.setCertificateFile(t.getCertificateFile());
        dto.setCertificateVerificationUrl(t.getCertificateVerificationUrl());
        dto.setCertificateId(t.getCertificateId());

        dto.setTrainingType(t.getTrainingType());

        dto.setCreatedAt(t.getCreatedAt());
        dto.setUpdatedAt(t.getUpdatedAt());

        dto.setUserProfileId(t.getUserProfile().getId());
        dto.setUserName(t.getUserProfile().getName());

        dto.setUserId(t.getUserProfile().getUser().getId());
        dto.setUserEmail(t.getUserProfile().getUser().getEmail());

        return dto;
    }

    public Training toEntity(TrainingRequestDTO dto) {
        Training t = new Training();


        t.setName(dto.getName());
        t.setDescription(dto.getDescription());

        t.setInstitution(dto.getInstitution());

        t.setStartDate(dto.getStartDate());
        t.setEndDate(dto.getEndDate());

        t.setCompleted(
                dto.getEndDate() != null && !dto.getEndDate().isAfter(LocalDate.now())
        );
        t.setDuration(dto.getDuration());


        t.setCertificateVerificationUrl(dto.getCertificateVerificationUrl());
        t.setCertificateId(dto.getCertificateId());

        t.setTrainingType(dto.getTrainingType());

        UserProfile userProfile = userProfileRepository.findById(dto.getUserProfileId())
                .orElseThrow(() -> new RuntimeException("No User Profile Found"));

        t.setUserProfile(userProfile);

        return t;
    }


}
