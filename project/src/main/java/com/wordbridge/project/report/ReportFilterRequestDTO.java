package com.wordbridge.project.report;

import com.wordbridge.project.enums.ReportStatus;
import com.wordbridge.project.enums.ReportType;
import com.wordbridge.project.enums.UserRole;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReportFilterRequestDTO {


    private String keyword;

    private Long userId;

    private UserRole userRole;

    private ReportType type;

    private ReportStatus status;

    private LocalDate createdFrom;

    private LocalDate createdTo;


}
