package com.wordbridge.project.saved;

import com.wordbridge.project.enums.UserRole;
import org.springframework.stereotype.Component;

@Component
public class SavedJobMapper {

    public SavedJobResponseDTO toDTO(SavedJob s) {
        SavedJobResponseDTO dto = new SavedJobResponseDTO();
        dto.setId(s.getId());
        dto.setCreatedAt(s.getCreatedAt());
        dto.setUserId(s.getUser().getId());
        if (s.getUser().getRole() == UserRole.USER) {
            dto.setUserName(s.getUser().getUserProfile().getName());
        } else if (s.getUser().getRole() == UserRole.COMPANY) {
            dto.setUserName(s.getUser().getCompanyProfile().getName());
        } else {
            throw new RuntimeException("Admin can't save Job");
        }
        dto.setJobId(s.getJob().getId());
        dto.setJobTitle(s.getJob().getTitle());
        dto.setJobDescription(s.getJob().getJobDescription());

        dto.setCompanyName(s.getJob().getCompanyProfile().getName());
        dto.setCompanyLogo(s.getJob().getCompanyProfile().getImage());


        return dto;
    }

}
