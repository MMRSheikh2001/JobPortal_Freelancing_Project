package com.wordbridge.project.wallet;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface WalletService {
    WalletResponseDTO createWallet(Long userId);

    WalletResponseDTO getById(Long id);

    WalletResponseDTO getByUserId(Long userId);

    BigDecimal getBalance(Long userId);

    BigDecimal getFrozenBalance(Long userId);

    WalletResponseDTO deposit(Long userId, BigDecimal amount);

    WalletResponseDTO withdraw(Long userId, BigDecimal amount);

    void freezeAmount(Long userId, BigDecimal amount);

    void unfreezeAmount(Long userId, BigDecimal amount);

    void transfer(Long fromUserId,
                  Long toUserId,
                  BigDecimal amount);

    void releasePayment(Long buyerId,
                        Long sellerId,
                        BigDecimal agreedPrice,
                        BigDecimal sellerAmount);

    void refundBuyer(Long buyerId,
                     BigDecimal amount);

    BigDecimal getTotalPlatformMoney();

}
