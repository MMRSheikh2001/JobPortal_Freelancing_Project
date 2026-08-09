package com.wordbridge.project.transaction;

import com.wordbridge.project.enums.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionResponseDTO {
    private Long id;
    private TransactionType type;
    private Long fromUserId;
    private String fromUserName;

    private Long toUserId;
    private String toUserName;

    private BigDecimal amount;

    private String description;

    private LocalDateTime createdAt;

}
