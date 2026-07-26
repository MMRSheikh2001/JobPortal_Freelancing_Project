package com.wordbridge.project.dashboard;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboards/")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;


    //========================================
    // User Dashboard
    //========================================

    @GetMapping("user/{userId}")
    public ResponseEntity<UserDashboardDTO> getUserDashboard(
            @PathVariable Long userId
    ) {

        return ResponseEntity.ok(
                dashboardService.getUserDashboard(userId)
        );

    }

    //Company Dashboard

    @GetMapping("company/{companyProfileId}")
    public CompanyDashboardDTO getCompanyDashboard(
            @PathVariable Long companyProfileId) {

        return dashboardService.getCompanyDashboard(companyProfileId);

    }

//========================================
// Admin Dashboard
//========================================

    @GetMapping("admin/{userId}")
    public ResponseEntity<AdminDashboardDTO> getAdminDashboard(
            @PathVariable Long userId
    ) {

        return ResponseEntity.ok(
                dashboardService.getAdminDashboard(userId)
        );

    }


//========================================
// Freelancer Dashboard
//========================================

    @GetMapping("freelancer/{userProfileId}")
    public ResponseEntity<FreelancerDashboardDTO> getFreelancerDashboard(
            @PathVariable Long userProfileId
    ) {

        return ResponseEntity.ok(
                dashboardService.getFreelancerDashboard(userProfileId)
        );

    }


    //Open api
    @GetMapping("/home-statistics")
    public ResponseEntity<HomeStatisticsDTO> getHomeStatistics() {

        return ResponseEntity.ok(
                dashboardService.getHomeStatistics()
        );

    }


}
