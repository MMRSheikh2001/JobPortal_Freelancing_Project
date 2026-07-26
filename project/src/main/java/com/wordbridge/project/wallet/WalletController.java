package com.wordbridge.project.wallet;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/wallets/")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;



    @GetMapping("/{id}")
    public WalletResponseDTO getById(@PathVariable Long id) {
        return walletService.getById(id);
    }

    @GetMapping("/user/{userId}")
    public WalletResponseDTO getByUserId(@PathVariable Long userId) {
        return walletService.getByUserId(userId);
    }

    @GetMapping("/user/{userId}/balance")
    public BigDecimal getBalance(@PathVariable Long userId) {
        return walletService.getBalance(userId);
    }

    @GetMapping("/user/{userId}/frozen-balance")
    public BigDecimal getFrozenBalance(@PathVariable Long userId) {
        return walletService.getFrozenBalance(userId);
    }



}
