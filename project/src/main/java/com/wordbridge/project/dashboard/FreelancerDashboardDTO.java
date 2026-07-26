package com.wordbridge.project.dashboard;

import com.wordbridge.project.gig.GigResponseDTO;
import com.wordbridge.project.gigorder.GigOrderResponseDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class FreelancerDashboardDTO {


    // =====================================
    // Profile
    // =====================================

    private String userName;

    private String profileImage;

    private Integer profileCompletion;

    // =====================================
    // Gig Statistics
    // =====================================

    private Long totalGigs;

    private Long activeGigs;

    private Long inactiveGigs;

    // =====================================
    // Orders
    // =====================================

    private Long totalOrders;

    private Long pendingOrders;

    private Long inProgressOrders;

    private Long deliveredOrders;

    private Long completedOrders;

    private Long cancelledOrders;

    private Long disputedOrders;

    // =====================================
    // Earnings
    // =====================================

    private BigDecimal walletBalance;

    private BigDecimal lifetimeEarnings;

    private BigDecimal pendingWithdrawals;

    // =====================================
    // Reviews
    // =====================================


    private Double averageRating;

    private Long totalReviews;

    // =====================================
    // Communication
    // =====================================
    private Long activeConversations;
    private Long unreadMessages;

    private Long unreadNotifications;

    // =====================================
    // Recent Activity
    // =====================================

    private List<GigOrderResponseDTO> recentOrders;

    private List<GigResponseDTO> myPopularGigs;


}
