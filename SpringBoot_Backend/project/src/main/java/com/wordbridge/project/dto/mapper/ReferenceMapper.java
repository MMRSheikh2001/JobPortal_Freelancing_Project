package com.wordbridge.project.dto.mapper;

import com.wordbridge.project.dto.requestdto.ReferenceRequestDTO;
import com.wordbridge.project.dto.responsedto.ReferenceResponseDTO;
import com.wordbridge.project.entity.Reference;
import com.wordbridge.project.entity.UserProfile;
import com.wordbridge.project.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReferenceMapper {
    private final UserProfileRepository userProfileRepository;

    public ReferenceResponseDTO toDTO(Reference r) {
        ReferenceResponseDTO dto = new ReferenceResponseDTO();
        dto.setId(r.getId());
        dto.setName(r.getName());
        dto.setDesignation(r.getDesignation());
        dto.setOrganization(r.getOrganization());
        dto.setPhone(r.getPhone());
        dto.setEmail(r.getEmail());
        dto.setAddress(r.getAddress());
        dto.setRelation(r.getRelation());

        dto.setUserProfileId(r.getUserProfile().getId());
        dto.setUserName(r.getUserProfile().getName());

        dto.setUserId(r.getUserProfile().getUser().getId());
        dto.setUserEmail(r.getUserProfile().getUser().getEmail());

        return dto;
    }

    public Reference toEntity(ReferenceRequestDTO dto) {
        Reference r = new Reference();

        r.setName(dto.getName());
        r.setDesignation(dto.getDesignation());
        r.setOrganization(dto.getOrganization());
        r.setPhone(dto.getPhone());
        r.setEmail(dto.getEmail());
        r.setAddress(dto.getAddress());
        r.setRelation(dto.getRelation());

        UserProfile userProfile = userProfileRepository.findById(dto.getUserProfileId())
                .orElseThrow(() -> new RuntimeException("No User Profile Found"));

        r.setUserProfile(userProfile);

        return r;
    }


}
