package com.wordbridge.project.dashboard;

import lombok.Data;

@Data
public class CompanyDashboardDTO {

    private String companyName;

    private String profileImage;

    private Integer profileCompletion;

    private Long totalJobs;

    private Long activeJobs;

    private Long inactiveJobs;

    private Long totalApplications;

    private Long appliedApplications;

    private Long aiCompletedApplications;

    private Long automaticQualifiedApplications;

    private Long companyShortlistedApplications;

    private Long hiredApplications;

    private Long unreadMessages;

    private Long unreadNotifications;
}
