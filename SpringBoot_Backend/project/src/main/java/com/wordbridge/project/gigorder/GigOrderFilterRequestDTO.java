package com.wordbridge.project.gigorder;

import com.wordbridge.project.enums.GigOrderStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GigOrderFilterRequestDTO {

    private String keyword;

    private Long buyerId;

    private Long sellerId;

    private Long gigId;

    private Long categoryId;

    private GigOrderStatus status;

    private Boolean paymentLocked;

    private LocalDate createdFrom;

    private LocalDate createdTo;

}
