package com.wordbridge.project.wallet;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class WalletResponseDTO {
    private Long id;

    private BigDecimal balance;
    private BigDecimal frozenBalance;

    private LocalDateTime createdAt;

    private Long userId;
    private String userName;

}
