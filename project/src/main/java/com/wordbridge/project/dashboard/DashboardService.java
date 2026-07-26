package com.wordbridge.project.dashboard;

import org.springframework.stereotype.Service;

@Service
public interface DashboardService {

    CompanyDashboardDTO getCompanyDashboard(Long companyProfileId);

    UserDashboardDTO getUserDashboard(Long userId);

    AdminDashboardDTO getAdminDashboard(Long userId);

    FreelancerDashboardDTO getFreelancerDashboard(Long userProfileId);

    HomeStatisticsDTO getHomeStatistics();

}
