package com.wordbridge.project.notification;

import com.wordbridge.project.enums.UserRole;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {


    public NotificationResponseDTO toDTO(Notification n) {
        NotificationResponseDTO dto = new NotificationResponseDTO();
        dto.setId(n.getId());
        dto.setUserId(n.getReceiver().getId());
        if (n.getReceiver().getRole() == UserRole.USER) {
            dto.setUserName(n.getReceiver().getUserProfile().getName());
        } else if (n.getReceiver().getRole() == UserRole.COMPANY) {
            dto.setUserName(n.getReceiver().getCompanyProfile().getName());
        } else {
            dto.setUserName("ADMIN");
        }
        dto.setTitle(n.getTitle());
        dto.setMessage(n.getMessage());
        dto.setType(n.getType());
        dto.setReferenceId(n.getReferenceId());
        dto.setIsRead(n.getIsRead());
        dto.setCreatedAt(n.getCreatedAt());


        return dto;
    }

}
