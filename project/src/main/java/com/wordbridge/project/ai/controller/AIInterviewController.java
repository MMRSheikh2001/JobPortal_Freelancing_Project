package com.wordbridge.project.ai.controller;

import com.wordbridge.project.ai.AIInterviewSessionResponseDTO;
import com.wordbridge.project.ai.service.AIInterviewService;
import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.UserRole;
import com.wordbridge.project.jobapplication.JobApplicationResponseDTO;
import com.wordbridge.project.jobapplication.JobApplicationService;
import com.wordbridge.project.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai/interview/")
@RequiredArgsConstructor
public class AIInterviewController {
    private final AIInterviewService aiInterviewService;

    private final JobApplicationService jobApplicationService;
    private final AuthenticationService authenticationService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("start/{applicationId}")
    public AIInterviewSessionResponseDTO startInterview(
            @PathVariable Long applicationId) {
        checkApplicantOnly(applicationId);

        return aiInterviewService.startInterview(applicationId);
    }


    @PreAuthorize("hasRole('USER')")
    @PostMapping("submit")
    public AIInterviewSessionResponseDTO submitInterview(
            @RequestBody AIInterviewSessionResponseDTO dto) {
        checkApplicantOnly(dto.getApplicationId());
        return aiInterviewService.submitInterview(dto);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("{applicationId}")
    public AIInterviewSessionResponseDTO getByApplicationId(@PathVariable Long applicationId) {
        checkApplicantOrHiringCompany(applicationId);
        return aiInterviewService.findByApplicationId(applicationId);
    }


    private void checkApplicantOnly(Long applicationId) {
        User currentUser = authenticationService.getCurrentUser();
        JobApplicationResponseDTO app = jobApplicationService.findById(applicationId);
        if (!app.getUserId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Not allowed");
        }
    }

    private void checkApplicantOrHiringCompany(Long applicationId) {
        User currentUser = authenticationService.getCurrentUser();
        if (currentUser.getRole() == UserRole.ADMIN) return;
        JobApplicationResponseDTO app = jobApplicationService.findById(applicationId);
        boolean isApplicant = app.getUserId().equals(currentUser.getId());
        boolean isHiringCompany = app.getCompanyUserId().equals(currentUser.getId());
        if (!isApplicant && !isHiringCompany) {
            throw new AccessDeniedException("Not allowed");
        }
    }


}
