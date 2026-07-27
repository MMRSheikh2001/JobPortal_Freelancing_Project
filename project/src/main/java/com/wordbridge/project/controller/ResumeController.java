package com.wordbridge.project.controller;

import com.wordbridge.project.dto.responsedto.ResumeResponseDTO;
import com.wordbridge.project.dto.responsedto.UserProfileResponseDTO;
import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.UserRole;
import com.wordbridge.project.security.AuthenticationService;
import com.wordbridge.project.service.ResumeService;
import com.wordbridge.project.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/resume/")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;
    private final UserProfileService userProfileService;
    private final AuthenticationService authenticationService;

    // Existing — returns JSON
    @PreAuthorize("isAuthenticated()")
    @GetMapping("{userProfileId}")
    public ResponseEntity<ResumeResponseDTO> generateResume(
            @PathVariable Long userProfileId) {
        checkProfileAccess(userProfileId);
        return ResponseEntity.ok(resumeService.generateResume(userProfileId));
    }

    // Preview — returns rendered HTML string
    // Angular can display this in an <iframe srcdoc="..."> or new tab
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "{userProfileId}/html",
            produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> generateHtml(
            @PathVariable Long userProfileId) {
        checkProfileAccess(userProfileId);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(resumeService.generateHtml(userProfileId));
    }

    // Download — returns PDF bytes as file download
    @PreAuthorize("isAuthenticated()")
    @GetMapping("{userProfileId}/pdf")
    public ResponseEntity<byte[]> generatePdf(
            @PathVariable Long userProfileId) {
        checkProfileAccess(userProfileId);
        byte[] pdf = resumeService.generatePdf(userProfileId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"cv.pdf\"")
                .body(pdf);
    }


    private void checkProfileAccess(Long userProfileId) {
        User currentUser = authenticationService.getCurrentUser();
        UserProfileResponseDTO profile = userProfileService.findById(userProfileId);
        boolean isOwner = profile.getUserId().equals(currentUser.getId());
        boolean isReviewer = currentUser.getRole() == UserRole.COMPANY || currentUser.getRole() == UserRole.ADMIN;
        if (!isOwner && !isReviewer) {
            throw new AccessDeniedException("Not allowed");
        }
    }

}