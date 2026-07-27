package com.wordbridge.project.controller;

import com.wordbridge.project.dto.requestdto.ResumeRequestDTO;
import com.wordbridge.project.dto.responsedto.ResumeFileResponseDTO;
import com.wordbridge.project.dto.responsedto.UserProfileResponseDTO;
import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.UserRole;
import com.wordbridge.project.security.AuthenticationService;
import com.wordbridge.project.service.ResumeFileService;
import com.wordbridge.project.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/resumes/uploadedfile/")
@RequiredArgsConstructor
public class ResumeFileController {
    private final ResumeFileService resumeFileService;
    private final UserProfileService userProfileService;   // NEW
    private final AuthenticationService authenticationService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<ResumeFileResponseDTO> uploadResume(
            @RequestParam Long userProfileId,
            @RequestParam MultipartFile cv) {

        checkOwnerOnly(userProfileId);

        ResumeRequestDTO dto = new ResumeRequestDTO();
        dto.setUserProfileId(userProfileId);

        ResumeFileResponseDTO response = resumeFileService.save(dto, cv);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<ResumeFileResponseDTO> findAll() {
        return resumeFileService.findAll();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("{id}")
    public ResponseEntity<ResumeFileResponseDTO> getById(@PathVariable Long id) {

        ResumeFileResponseDTO dto = resumeFileService.getById(id);
        checkProfileAccess(dto.getUserProfileId());
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        ResumeFileResponseDTO dto = resumeFileService.getById(id);
        checkOwnerOnly(dto.getUserProfileId());
        resumeFileService.delete(id);
        return ResponseEntity.ok("Resume deleted");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/{userProfileId}")
    public ResumeFileResponseDTO findByUserProfileId(@PathVariable Long userProfileId) {
        checkProfileAccess(userProfileId);
        return resumeFileService.findByUserProfileId(userProfileId);
    }


    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/user/{userProfileId}")
    public ResponseEntity<String> deleteByUserProfileId(@PathVariable Long userProfileId) {
        checkOwnerOnly(userProfileId);
        resumeFileService.deleteByUserProfileId(userProfileId);
        return ResponseEntity.ok("Resume deleted successfully");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/exists/{userProfileId}")
    public ResponseEntity<Boolean> exists(@PathVariable Long userProfileId) {
        checkProfileAccess(userProfileId);
        return ResponseEntity.ok(resumeFileService.existsByUserProfileId(userProfileId));
    }


    //  owner OR company/admin reviewer (reads)
    private void checkProfileAccess(Long userProfileId) {
        User currentUser = authenticationService.getCurrentUser();
        UserProfileResponseDTO profile = userProfileService.findById(userProfileId);
        boolean isOwner = profile.getUserId().equals(currentUser.getId());
        boolean isReviewer = currentUser.getRole() == UserRole.COMPANY || currentUser.getRole() == UserRole.ADMIN;
        if (!isOwner && !isReviewer) {
            throw new AccessDeniedException("Not allowed");
        }
    }

    // owner only (uploads/deletes)
    private void checkOwnerOnly(Long userProfileId) {
        User currentUser = authenticationService.getCurrentUser();
        UserProfileResponseDTO profile = userProfileService.findById(userProfileId);
        if (!profile.getUserId().equals(currentUser.getId()) && currentUser.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Not allowed");
        }
    }



}
