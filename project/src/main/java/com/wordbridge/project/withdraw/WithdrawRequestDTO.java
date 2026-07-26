package com.wordbridge.project.withdraw;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithdrawRequestDTO {

    private Long userId;
    private BigDecimal amount;

    private WithdrawMethod withdrawMethod;

    private String accountNumber;
    private String accountName;

}
