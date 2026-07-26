package com.wordbridge.project.dashboard;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AdminDashboardDTO {

    // =============================
    // Platform Statistics
    // =============================

    private Long totalUsers;

    private Long totalJobSeekers;

    private Long totalCompanies;

    private Long totalFreelancers;

    private Long totalClients;

    private Long totalActiveFreelancers;

    private Long totalActiveClients;

    // =============================
    // Job Module
    // =============================

    private Long totalJobs;

    private Long activeJobs;

    private Long inactiveJobs;

    private Long totalJobApplications;

    private Long totalHiredCandidates;

    // =============================
    // Freelance Module
    // =============================

    private Long totalGigs;

    private Long activeGigs;

    private Long inactiveGigs;

    private Long totalOrders;

    private Long completedOrders;

    private Long cancelledOrders;

    // =============================
    // Financial
    // =============================

    private BigDecimal totalPlatformRevenue;

    private BigDecimal pendingWithdrawals;

    private BigDecimal completedWithdrawals;

    // =============================
    // AI Statistics
    // =============================

    private Long totalAIInterviews;

    private Long totalAIMatches;

    private Long totalAIQualifiedCandidates;

    // =============================
    // Moderation
    // =============================

    private Long pendingReports;

    private Long pendingDisputes;

    private Long blockedUsers;

    // =============================
    // System
    // =============================

    private Long unreadNotifications;

}