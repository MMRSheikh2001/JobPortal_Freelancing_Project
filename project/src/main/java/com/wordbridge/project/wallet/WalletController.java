package com.wordbridge.project.wallet;

import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.UserRole;
import com.wordbridge.project.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/wallets/")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    private final AuthenticationService authenticationService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public WalletResponseDTO getById(@PathVariable Long id) {
        WalletResponseDTO wallet = walletService.getById(id);
        checkUserIdOwnership(wallet.getUserId());
        return wallet;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/{userId}")
    public WalletResponseDTO getByUserId(@PathVariable Long userId) {
        checkUserIdOwnership(userId);
        return walletService.getByUserId(userId);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/{userId}/balance")
    public BigDecimal getBalance(@PathVariable Long userId) {
        checkUserIdOwnership(userId);
        return walletService.getBalance(userId);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/{userId}/frozen-balance")
    public BigDecimal getFrozenBalance(@PathVariable Long userId) {
        checkUserIdOwnership(userId);
        return walletService.getFrozenBalance(userId);
    }


    private void checkUserIdOwnership(Long userId) {
        User currentUser = authenticationService.getCurrentUser();
        if (!currentUser.getId().equals(userId) && currentUser.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Not allowed");
        }
    }

}
