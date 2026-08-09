package com.MMRSheikh2001.workbridgeandroid.response;


import java.util.List;

import lombok.Data;

@Data
public class UserDashboardDTO {


    private String userName;
    private String profileImage;
    private Integer profileCompletion;

    private Long appliedJobs;

    private Long savedJobs;

    private Long savedGigs;

    private Long activeOrders;

    private Long unreadMessages;

    private Long unreadNotifications;

    private List<JobApplicationResponseDTO> recentApplications;

    private List<GigOrderResponseDTO> recentOrders;

    private List<JobResponseDTO> latestJobs;

    private List<GigResponseDTO> popularGigs;


}