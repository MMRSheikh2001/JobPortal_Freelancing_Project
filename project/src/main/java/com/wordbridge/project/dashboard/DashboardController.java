package com.wordbridge.project.dashboard;

import com.wordbridge.project.entity.User;
import com.wordbridge.project.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboards/")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    private final AuthenticationService authenticationService;


    //========================================
    // User Dashboard
    //========================================

    @PreAuthorize("isAuthenticated()")
    @GetMapping("user/{userId}")
    public ResponseEntity<UserDashboardDTO> getUserDashboard(
            @PathVariable Long userId
    ) {
        User currentUser=authenticationService.getCurrentUser();
        if (!currentUser.getId().equals(userId) ) {
            throw new AccessDeniedException("Not allowed");
        }

        return ResponseEntity.ok(
                dashboardService.getUserDashboard(userId)
        );

    }

    //Company Dashboard

    @PreAuthorize("hasRole('COMPANY')")
    @GetMapping("company/{companyProfileId}")
    public CompanyDashboardDTO getCompanyDashboard(
            @PathVariable Long companyProfileId) {
        User currentUser=authenticationService.getCurrentUser();
        if (!currentUser.getCompanyProfile().getId().equals(companyProfileId) ) {
            throw new AccessDeniedException("Not allowed");
        }

        return dashboardService.getCompanyDashboard(companyProfileId);

    }

//========================================
// Admin Dashboard
//========================================

    @PreAuthorize("hasRole('ADMIN')")
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

    @PreAuthorize("hasRole('USER')")
    @GetMapping("freelancer/{userProfileId}")
    public ResponseEntity<FreelancerDashboardDTO> getFreelancerDashboard(
            @PathVariable Long userProfileId
    ) {
        User currentUser=authenticationService.getCurrentUser();
        if (!currentUser.getUserProfile().getId().equals(userProfileId) ) {
            throw new AccessDeniedException("Not allowed");
        }
        return ResponseEntity.ok(
                dashboardService.getFreelancerDashboard(userProfileId)
        );

    }


    //Open api
    @PreAuthorize("permitAll()")
    @GetMapping("/home-statistics")
    public ResponseEntity<HomeStatisticsDTO> getHomeStatistics() {

        return ResponseEntity.ok(
                dashboardService.getHomeStatistics()
        );

    }


}
