package com.wordbridge.project.dto.mapper;

import com.wordbridge.project.dto.requestdto.EducationRequestDTO;
import com.wordbridge.project.dto.responsedto.EducationResponseDTO;
import com.wordbridge.project.entity.Education;
import com.wordbridge.project.entity.UserProfile;
import com.wordbridge.project.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class EducationMapper {
    private final UserProfileRepository userProfileRepository;


    public EducationResponseDTO toDTO(Education e) {
        EducationResponseDTO dto = new EducationResponseDTO();

        dto.setId(e.getId());
        dto.setEducationLevel(e.getEducationLevel());
        dto.setBoard(e.getBoard());
        dto.setInstitution(e.getInstitution());
        dto.setFieldOfStudy(e.getFieldOfStudy());
        dto.setResultType(e.getResultType());
        dto.setResult(e.getResult());
        dto.setOutOf(e.getOutOf());
        dto.setGradeOrDivision(e.getGradeOrDivision());
        dto.setStartDate(e.getStartDate());
        dto.setEndDate(e.getEndDate());
        if (dto.getEndDate() == null || dto.getEndDate().isAfter(LocalDate.now())) {
            e.setCurrentlyStudying(true);
        }

        dto.setCurrentlyStudying(e.getCurrentlyStudying());
        dto.setCreatedAt(e.getCreatedAt());
        dto.setUserProfileId(e.getUserProfile().getId());
        dto.setUserId(e.getUserProfile().getUser().getId());

        dto.setUserName(e.getUserProfile().getName());
        dto.setUserEmail(e.getUserProfile().getUser().getEmail());


        return dto;
    }

    public Education toEntity(EducationRequestDTO dto) {
        Education e = new Education();
        e.setEducationLevel(dto.getEducationLevel());
        e.setBoard(dto.getBoard());
        e.setInstitution(dto.getInstitution());
        e.setFieldOfStudy(dto.getFieldOfStudy());
        e.setResultType(dto.getResultType());
        e.setResult(dto.getResult());
        e.setOutOf(dto.getOutOf());
        e.setGradeOrDivision(dto.getGradeOrDivision());
        e.setStartDate(dto.getStartDate());
        e.setEndDate(dto.getEndDate());

        e.setCurrentlyStudying(false);

        if (dto.getEndDate() == null || dto.getEndDate().isAfter(LocalDate.now())) {
            e.setCurrentlyStudying(true);
        }

        UserProfile userProfile = userProfileRepository.findById(dto.getUserProfileId())
                .orElseThrow(() -> new RuntimeException("User Profile Not Found"));

        e.setUserProfile(userProfile);


        return e;
    }

}
