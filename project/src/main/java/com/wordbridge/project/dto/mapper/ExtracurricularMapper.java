package com.wordbridge.project.dto.mapper;

import com.wordbridge.project.dto.requestdto.ExtracurricularRequestDTO;
import com.wordbridge.project.dto.responsedto.ExtracurricularResponseDTO;
import com.wordbridge.project.entity.Extracurricular;
import com.wordbridge.project.entity.UserProfile;
import com.wordbridge.project.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExtracurricularMapper {
    private final UserProfileRepository userProfileRepository;


    public ExtracurricularResponseDTO toDTO(Extracurricular e) {
        ExtracurricularResponseDTO dto = new ExtracurricularResponseDTO();
        dto.setId(e.getId());
        dto.setTitle(e.getTitle());
        dto.setDescription(e.getDescription());

        dto.setOrganization(e.getOrganization());
        dto.setRole(e.getRole());


        dto.setUserProfileId(e.getUserProfile().getId());
        dto.setUserId(e.getUserProfile().getUser().getId());

        dto.setUserName(e.getUserProfile().getName());
        dto.setUserEmail(e.getUserProfile().getUser().getEmail());


        return dto;
    }

    public Extracurricular toEntity(ExtracurricularRequestDTO dto) {
        Extracurricular e = new Extracurricular();

        e.setTitle(dto.getTitle());
        e.setDescription(dto.getDescription());

        e.setOrganization(dto.getOrganization());
        e.setRole(dto.getRole());

        UserProfile userProfile = userProfileRepository.findById(dto.getUserProfileId())
                .orElseThrow(() -> new RuntimeException("No User Profile found"));
        e.setUserProfile(userProfile);

        return e;
    }

}
