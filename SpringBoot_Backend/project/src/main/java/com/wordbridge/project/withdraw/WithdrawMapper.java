package com.wordbridge.project.withdraw;

import com.wordbridge.project.enums.UserRole;
import org.springframework.stereotype.Component;

@Component
public class WithdrawMapper {

    public WithdrawResponseDTO toDTO(Withdraw w) {
        WithdrawResponseDTO dto = new WithdrawResponseDTO();
        dto.setId(w.getId());
        dto.setWalletId(w.getWallet().getId());
        dto.setWalletBalance(w.getWallet().getBalance());
        dto.setUserId(w.getWallet().getUser().getId());
        dto.setUserEmail(w.getWallet().getUser().getEmail());
        dto.setUserRole(w.getWallet().getUser().getRole());
        if (dto.getUserRole() == UserRole.USER) {
            dto.setUserName(w.getWallet().getUser().getUserProfile().getName());
        } else if (dto.getUserRole() == UserRole.COMPANY) {
            dto.setUserName(w.getWallet().getUser().getCompanyProfile().getName());
        } else {
            throw new RuntimeException("Admin can not withdraw");
        }
        dto.setAmount(w.getAmount());
        dto.setWithdrawMethod(w.getWithdrawMethod());
        dto.setAccountNumber(w.getAccountNumber());
        dto.setAccountName(w.getAccountName());
        dto.setWithdrawStatus(w.getWithdrawStatus());
        dto.setCreatedAt(w.getCreatedAt());
        dto.setUpdatedAt(w.getUpdatedAt());
        dto.setAdminRemarks(w.getAdminRemarks());
        dto.setTransactionReference(w.getTransactionReference());

        return dto;
    }

}
