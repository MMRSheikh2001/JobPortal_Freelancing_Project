package com.wordbridge.project.resumeimport;

import com.wordbridge.project.dto.responsedto.ResumeFileResponseDTO;
import com.wordbridge.project.dto.responsedto.ResumeResponseDTO;
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

@RestController
@RequestMapping("/api/resume-import/")
@RequiredArgsConstructor
public class ResumeImportController {
    private final ResumeImportService resumeImportService;
    private final AuthenticationService authenticationService;
    private final UserProfileService userProfileService;
    private final ResumeFileService resumeFileService;


    @PreAuthorize("hasRole('USER')")
    @GetMapping("{resumeId}")
    public ResponseEntity<ResumeImportPreviewDTO> getJSONFromResume(@PathVariable Long resumeId) {

        ResumeFileResponseDTO resumeResponseDTO = resumeFileService.getById(resumeId);

        checkProfileOwnership(resumeResponseDTO.getUserProfileId());


        return ResponseEntity.ok(resumeImportService.getPreviewFromGemini(resumeId));

    }


    @PreAuthorize("hasRole('USER')")
    @PostMapping("/save/{userProfileId}")
    public ResponseEntity<Void> saveImportedResume(
            @PathVariable Long userProfileId,
            @RequestBody ResumeImportPreviewDTO preview) {

        checkProfileOwnership(userProfileId);
        resumeImportService.saveImportedResume(userProfileId, preview);

        return ResponseEntity.ok().build();
    }


    private void checkProfileOwnership(Long userProfileId) {
        User currentUser = authenticationService.getCurrentUser();
        UserProfileResponseDTO profile = userProfileService.findById(userProfileId);
        if (!profile.getUserId().equals(currentUser.getId()) && currentUser.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Not allowed");
        }
    }

//    @GetMapping("{resumeId}")
//    public ResponseEntity<String> getTextFromResume(@PathVariable Long resumeId){
//
//        return ResponseEntity.ok(resumeImportService.extractTextFromCV(resumeId));
//
//    }


}
