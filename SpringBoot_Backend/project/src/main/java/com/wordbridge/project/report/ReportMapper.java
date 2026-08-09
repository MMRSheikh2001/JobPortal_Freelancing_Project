package com.wordbridge.project.report;

import com.wordbridge.project.enums.UserRole;
import org.springframework.stereotype.Component;

@Component
public class ReportMapper {
    public ReportResponseDTO toDTO(Report r) {
        ReportResponseDTO dto = new ReportResponseDTO();

        dto.setId(r.getId());
        dto.setType(r.getType());
        dto.setSubject(r.getSubject());
        dto.setDescription(r.getDescription());
        dto.setAttachmentUrl(r.getAttachmentUrl());

        dto.setStatus(r.getStatus());
        dto.setAdminReply(r.getAdminReply());
        dto.setCreatedAt(r.getCreatedAt());
        dto.setResolvedAt(r.getResolvedAt());

        dto.setUserId(r.getUser().getId());
        dto.setUserEmail(r.getUser().getEmail());
        dto.setUserRole(r.getUser().getRole());
        if (dto.getUserRole() == UserRole.USER) {
            dto.setUserName(r.getUser().getUserProfile().getName());
            dto.setProfileId(r.getUser().getUserProfile().getId());
        } else {
            dto.setUserName(r.getUser().getCompanyProfile().getName());
            dto.setProfileId(r.getUser().getCompanyProfile().getId());
        }


        return dto;

    }


}
