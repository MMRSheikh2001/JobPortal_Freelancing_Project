package com.wordbridge.project.controller;

import com.wordbridge.project.dto.requestdto.EducationRequestDTO;
import com.wordbridge.project.dto.responsedto.EducationResponseDTO;
import com.wordbridge.project.dto.responsedto.UserProfileResponseDTO;
import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.UserRole;
import com.wordbridge.project.security.AuthenticationService;
import com.wordbridge.project.service.EducationService;
import com.wordbridge.project.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/educations/")
@RequiredArgsConstructor
public class EducationController {
    private final EducationService educationService;

    private final UserProfileService userProfileService;
    private final AuthenticationService authenticationService;


    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<EducationResponseDTO> save(@RequestBody EducationRequestDTO dto) {
        checkProfileOwnership(dto.getUserProfileId());
        EducationResponseDTO saved = educationService.save(dto);
        return ResponseEntity.ok(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<EducationResponseDTO>> getAll() {
        List<EducationResponseDTO> list = educationService.getAll();
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("{id}")
    public ResponseEntity<EducationResponseDTO> getById(@PathVariable Long id) {
        checkRecordOwnership(id);
        EducationResponseDTO ul = educationService.findById(id);
        return ResponseEntity.ok(ul);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("{id}")
    public ResponseEntity<EducationResponseDTO> update(@RequestBody EducationRequestDTO ul, @PathVariable Long id) {

        checkRecordOwnership(id);
        EducationResponseDTO updated = educationService.update(id, ul);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        checkRecordOwnership(id);
        educationService.delete(id);
        return ResponseEntity.ok("User Education Deleted");
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("userprofile/{id}")
    public List<EducationResponseDTO> findByUserProfileId(@PathVariable Long id) {
        return educationService.findByUserProfileId(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("userprofile/count/{id}")
    public Long countByUserProfileId(@PathVariable Long id) {
        return educationService.countByUserProfileId(id);
    }


    private void checkRecordOwnership(Long educationId) {
        User currentUser = authenticationService.getCurrentUser();
        EducationResponseDTO existing = educationService.findById(educationId);
        if (!existing.getUserId().equals(currentUser.getId()) && currentUser.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Not allowed");
        }
    }

    private void checkProfileOwnership(Long userProfileId) {
        User currentUser = authenticationService.getCurrentUser();
        UserProfileResponseDTO profile = userProfileService.findById(userProfileId);
        if (!profile.getUserId().equals(currentUser.getId()) && currentUser.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Not allowed");
        }
    }
}
