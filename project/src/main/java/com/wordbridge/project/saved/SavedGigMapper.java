package com.wordbridge.project.saved;

import com.wordbridge.project.enums.UserRole;
import org.springframework.stereotype.Component;

@Component
public class SavedGigMapper {

    public SavedGigResponseDTO toDTO(SavedGig s) {
        SavedGigResponseDTO dto = new SavedGigResponseDTO();

        dto.setId(s.getId());
        dto.setCreatedAt(s.getCreatedAt());
        dto.setUserId(s.getUser().getId());
        if (s.getUser().getRole() == UserRole.USER) {
            dto.setUserName(s.getUser().getUserProfile().getName());
        } else if (s.getUser().getRole() == UserRole.COMPANY) {
            dto.setUserName(s.getUser().getCompanyProfile().getName());
        } else {
            throw new RuntimeException("Admin can't save gig");
        }
        dto.setGigId(s.getGig().getId());
        dto.setGigTitle(s.getGig().getTitle());
        dto.setGigDescription(s.getGig().getShortDescription());
        dto.setFreelancerName(s.getGig().getUserProfile().getName());
        dto.setGigImage(s.getGig().getGigImage());

        return dto;
    }

}
