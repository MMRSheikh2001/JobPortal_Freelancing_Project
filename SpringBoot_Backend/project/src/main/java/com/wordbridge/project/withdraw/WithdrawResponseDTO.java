package com.wordbridge.project.withdraw;

import com.wordbridge.project.enums.UserRole;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class WithdrawResponseDTO {

    private Long id;
    private Long walletId;
    private BigDecimal walletBalance;

    private Long userId;
    private String userName;
    private String userEmail;
    private UserRole userRole;

    private BigDecimal amount;

    private WithdrawMethod withdrawMethod;

    private String accountNumber;
    private String accountName;

    private WithdrawStatus withdrawStatus;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String adminRemarks;

    private String transactionReference;

}
