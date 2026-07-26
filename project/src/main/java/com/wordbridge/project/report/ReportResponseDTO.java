package com.wordbridge.project.report;

import com.wordbridge.project.enums.ReportStatus;
import com.wordbridge.project.enums.ReportType;
import com.wordbridge.project.enums.UserRole;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportResponseDTO {


    private Long id;

    private ReportType type;

    private String subject;

    private String description;

    private String attachmentUrl;

    private ReportStatus status;

    private String adminReply;

    private LocalDateTime createdAt;

    private LocalDateTime resolvedAt;

    // Reporter
    private Long userId;

    private String userName;
    private Long profileId;

    private UserRole userRole;

    private String userEmail;

}
