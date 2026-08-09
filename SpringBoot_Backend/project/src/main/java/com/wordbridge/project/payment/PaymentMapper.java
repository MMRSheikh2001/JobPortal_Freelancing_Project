package com.wordbridge.project.payment;

import com.wordbridge.project.enums.UserRole;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentResponseDTO toDTO(Payment p) {
        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setId(p.getId());

        dto.setPaymentStatus(p.getPaymentStatus());
        dto.setCreatedAt(p.getCreatedAt());
        dto.setUpdatedAt(p.getUpdatedAt());

        dto.setAmount(p.getAmount());
        if (p.getUser() != null) {
            dto.setUserId(p.getUser().getId());

            if (p.getUser().getRole() == UserRole.USER) {
                dto.setUserName(p.getUser().getUserProfile().getName());
                dto.setUserRole(UserRole.USER);
            } else if (p.getUser().getRole() == UserRole.COMPANY) {
                dto.setUserName(p.getUser().getCompanyProfile().getName());
                dto.setUserRole(UserRole.COMPANY);
            } else {
                dto.setUserName("ADMIN");
            }
        }
        dto.setGatewayTransactionId(p.getGatewayTransactionId());
        dto.setValidationId(p.getValidationId());
        dto.setPaymentMethod(p.getPaymentMethod());
        dto.setGateway(p.getGateway());
        dto.setFailureReason(p.getFailureReason());


        return dto;
    }

}
