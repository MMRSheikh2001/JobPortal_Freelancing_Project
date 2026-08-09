package com.wordbridge.project.dashboard;

import lombok.Data;

@Data
public class HomeStatisticsDTO {

    // Platform
    private Long totalUsers;

    private Long totalCompanies;

    private Long totalFreelancers;

    // Jobs
    private Long totalJobs;

    private Long activeJobs;

    // Freelancing
    private Long totalGigs;

    private Long activeGigs;

    // Marketplace
    private Long completedOrders;

}
