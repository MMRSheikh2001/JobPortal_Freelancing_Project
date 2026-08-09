package com.wordbridge.project.transaction;

import com.wordbridge.project.enums.TransactionType;
import com.wordbridge.project.enums.UserRole;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TransactionFilterDTO {


    private TransactionType transactionType;

    private UserRole userRole;

    private String keyword;

    private LocalDate fromDate;

    private LocalDate toDate;

    private Long userId;
}
