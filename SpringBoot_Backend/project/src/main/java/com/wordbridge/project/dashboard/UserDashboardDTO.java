package com.wordbridge.project.dashboard;

import com.wordbridge.project.gig.GigResponseDTO;
import com.wordbridge.project.gigorder.GigOrderResponseDTO;
import com.wordbridge.project.job.JobResponseDTO;
import com.wordbridge.project.jobapplication.JobApplicationResponseDTO;
import lombok.Data;

import java.util.List;

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
