package com.wordbridge.project.gigorder;

import com.wordbridge.project.enums.UserRole;
import org.springframework.stereotype.Component;

@Component
public class GigOrderMapper {

    public GigOrderResponseDTO toDTO(GigOrder g) {
        GigOrderResponseDTO dto = new GigOrderResponseDTO();
        dto.setId(g.getId());
        dto.setQuotedPrice(g.getQuotedPrice());
        dto.setAgreedPrice(g.getAgreedPrice());
        dto.setFinalPrice(g.getFinalPrice());
        dto.setDeliveryMessage(g.getDeliveryMessage());
        dto.setDeliveryFileUrl(g.getDeliveryFileUrl());
        dto.setPaymentLocked(g.getPaymentLocked());
        dto.setStatus(g.getStatus());

        dto.setCreatedAt(g.getCreatedAt());
        dto.setQuotedAt(g.getQuotedAt());
        dto.setQuoteAcceptedAt(g.getQuoteAcceptedAt());
        dto.setExpectedDeliveryAt(g.getExpectedDeliveryAt());

        dto.setDeliveredAt(g.getDeliveredAt());
        dto.setBuyerAcceptedAt(g.getBuyerAcceptedAt());
        dto.setBuyerRejectedAt(g.getBuyerRejectedAt());
        dto.setBuyerCancelledAt(g.getBuyerCancelledAt());

        dto.setSellerCancelledAt(g.getSellerCancelledAt());
        dto.setSellerDisputeOpenedAt(g.getSellerDisputeOpenedAt());
        dto.setSellerDisputeDeadline(g.getSellerDisputeDeadline());

        dto.setPaymentReleasedAt(g.getPaymentReleasedAt());
        dto.setRefundedAt(g.getRefundedAt());


        dto.setGigId(g.getGig().getId());
        dto.setGigTitle(g.getGig().getTitle());
        dto.setGigImage(g.getGig().getGigImage());

        dto.setSellerId(g.getGig().getUserProfile().getId());
        dto.setSellerName(g.getGig().getUserProfile().getName());

        dto.setBuyerId(g.getBuyer().getId());


        if (g.getBuyer().getRole() == UserRole.USER) {
            dto.setBuyerName(g.getBuyer().getUserProfile().getName());
            dto.setBuyerUserProfileId(g.getBuyer().getUserProfile().getId());
            dto.setBuyerRole(UserRole.USER);
        } else {
            dto.setBuyerName(g.getBuyer().getCompanyProfile().getName());
            dto.setBuyerCompanyProfileId(g.getBuyer().getCompanyProfile().getId());
            dto.setBuyerRole(UserRole.COMPANY);
        }
        dto.setConversationId(g.getConversation().getId());

        return dto;
    }


}
