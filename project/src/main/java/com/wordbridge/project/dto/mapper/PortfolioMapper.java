package com.wordbridge.project.dto.mapper;

import com.wordbridge.project.dto.requestdto.PortfolioRequestDTO;
import com.wordbridge.project.dto.responsedto.PortfolioResponseDTO;
import com.wordbridge.project.entity.Portfolio;
import com.wordbridge.project.entity.UserProfile;
import com.wordbridge.project.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PortfolioMapper {
    private final UserProfileRepository userProfileRepository;

    public PortfolioResponseDTO toDTO(Portfolio p) {
        PortfolioResponseDTO dto = new PortfolioResponseDTO();

        dto.setId(p.getId());
        dto.setTitle(p.getTitle());
        dto.setDescription(p.getDescription());
        dto.setProjectUrl(p.getProjectUrl());
        dto.setFileUrl(p.getFileUrl());
        dto.setTechnologies(p.getTechnologies());
        dto.setCreatedAt(p.getCreatedAt());
        dto.setUpdatedAt(p.getUpdatedAt());

        dto.setUserProfileId(p.getUserProfile().getId());
        dto.setUserName(p.getUserProfile().getName());

        dto.setUserId(p.getUserProfile().getUser().getId());
        dto.setUserEmail(p.getUserProfile().getUser().getEmail());


        return dto;
    }

    public Portfolio toEntity(PortfolioRequestDTO dto) {
        Portfolio p = new Portfolio();

        p.setTitle(dto.getTitle());
        p.setDescription(dto.getDescription());
        p.setProjectUrl(dto.getProjectUrl());

        p.setTechnologies(dto.getTechnologies());

        UserProfile userProfile = userProfileRepository.findById(dto.getUserProfileId())
                .orElseThrow(() -> new RuntimeException("User Profile Not Found"));

        p.setUserProfile(userProfile);


        return p;
    }

}
