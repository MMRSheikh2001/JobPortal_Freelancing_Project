package com.wordbridge.project.controller;

import com.wordbridge.project.dto.requestdto.ExperienceRequestDTO;
import com.wordbridge.project.dto.responsedto.ExperienceResponseDTO;
import com.wordbridge.project.dto.responsedto.UserProfileResponseDTO;
import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.UserRole;
import com.wordbridge.project.security.AuthenticationService;
import com.wordbridge.project.service.ExperienceService;
import com.wordbridge.project.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/experiences/")
@RequiredArgsConstructor
public class ExperienceController {
    private final ExperienceService experienceService;

    private final UserProfileService userProfileService;
    private final AuthenticationService authenticationService;


    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<ExperienceResponseDTO> save(@RequestBody ExperienceRequestDTO dto) {
        checkProfileOwnership(dto.getUserProfileId());
        ExperienceResponseDTO saved = experienceService.save(dto);
        return ResponseEntity.ok(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ExperienceResponseDTO>> getAll() {
        List<ExperienceResponseDTO> list = experienceService.getAll();
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("{id}")
    public ResponseEntity<ExperienceResponseDTO> getById(@PathVariable Long id) {
        checkRecordOwnership(id);
        ExperienceResponseDTO ul = experienceService.findById(id);
        return ResponseEntity.ok(ul);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("{id}")
    public ResponseEntity<ExperienceResponseDTO> update(@RequestBody ExperienceRequestDTO dto, @PathVariable Long id) {

        checkRecordOwnership(id);
        ExperienceResponseDTO updated = experienceService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        checkRecordOwnership(id);
        experienceService.delete(id);
        return ResponseEntity.ok("User Experience Deleted");
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("userprofile/{id}")
    public List<ExperienceResponseDTO> findByUserProfileId(@PathVariable Long id) {
        return experienceService.findByUserProfileId(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("userprofile/count/{id}")
    public Long countByUserProfileId(@PathVariable Long id) {
        return experienceService.countByUserProfileId(id);
    }


    private void checkRecordOwnership(Long experienceId) {
        User currentUser = authenticationService.getCurrentUser();
        ExperienceResponseDTO existing = experienceService.findById(experienceId);
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
