package com.wordbridge.project.conversation;

import com.wordbridge.project.enums.UserRole;
import org.springframework.stereotype.Component;

@Component
public class ConversationMapper {
    public ConversationResponseDTO toDTO(Conversation c) {
        ConversationResponseDTO dto = new ConversationResponseDTO();

        dto.setId(c.getId());
        dto.setCreatedAt(c.getCreatedAt());
        dto.setLastMessageAt(c.getLastMessageAt());
        dto.setConversationStatus(c.getStatus());

        dto.setGigOrderId(c.getGigOrder().getId());
        dto.setStatus(c.getGigOrder().getStatus());

        dto.setGigId(c.getGigOrder().getGig().getId());
        dto.setGigTitle(c.getGigOrder().getGig().getTitle());
        dto.setGigImage(c.getGigOrder().getGig().getGigImage());

        dto.setSellerUserProfileId(c.getGigOrder().getGig().getUserProfile().getId());
        dto.setSellerName(c.getGigOrder().getGig().getUserProfile().getName());

        dto.setBuyerId(c.getGigOrder().getBuyer().getId());
        if (c.getGigOrder().getBuyer().getRole() == UserRole.USER) {
            dto.setBuyerName(c.getGigOrder().getBuyer().getUserProfile().getName());
        } else {
            dto.setBuyerName(c.getGigOrder().getBuyer().getCompanyProfile().getName());
        }


        return dto;
    }

}
