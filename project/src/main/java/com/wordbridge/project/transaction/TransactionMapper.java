package com.wordbridge.project.transaction;

import com.wordbridge.project.enums.UserRole;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionResponseDTO toDTO(Transaction t) {
        TransactionResponseDTO dto = new TransactionResponseDTO();

        dto.setId(t.getId());
        dto.setType(t.getType());

        if (t.getFromUser() != null) {

            dto.setFromUserId(t.getFromUser().getId());

            if (t.getFromUser().getRole() == UserRole.USER) {
                dto.setFromUserName(t.getFromUser().getUserProfile().getName());
            } else if (t.getFromUser().getRole() == UserRole.COMPANY) {
                dto.setFromUserName(t.getFromUser().getCompanyProfile().getName());
            } else {
                dto.setFromUserName("ADMIN");
            }

        } else {

            dto.setFromUserName("SSLCommerz");

        }
        if (t.getToUser() != null) {

            dto.setToUserId(t.getToUser().getId());
            if (t.getToUser().getRole() == UserRole.USER) {
                dto.setToUserName(t.getToUser().getUserProfile().getName());
            } else if (t.getToUser().getRole() == UserRole.COMPANY) {
                dto.setToUserName(t.getToUser().getCompanyProfile().getName());
            } else {
                dto.setToUserName("ADMIN");
            }

        } else {

            dto.setToUserName("SSLCommerz");

        }


        dto.setAmount(t.getAmount());
        dto.setDescription(t.getDescription());
        dto.setCreatedAt(t.getCreatedAt());


        return dto;
    }

}
