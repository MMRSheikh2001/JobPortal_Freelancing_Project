package com.wordbridge.project.wallet;

import com.wordbridge.project.enums.UserRole;
import org.springframework.stereotype.Component;

@Component
public class WalletMapper {

    public WalletResponseDTO toDTO(Wallet w) {
        WalletResponseDTO dto = new WalletResponseDTO();

        dto.setId(w.getId());
        dto.setBalance(w.getBalance());
        dto.setFrozenBalance(w.getFrozenBalance());
        dto.setCreatedAt(w.getCreatedAt());
        dto.setUserId(w.getUser().getId());
        if (w.getUser().getRole() == UserRole.USER) {
            dto.setUserName(w.getUser().getUserProfile().getName());
        }
        if (w.getUser().getRole() == UserRole.COMPANY) {
            dto.setUserName(w.getUser().getCompanyProfile().getName());

        }
        if (w.getUser().getRole() == UserRole.ADMIN) {
            dto.setUserName("ADMIN");
        }


        return dto;
    }

}
