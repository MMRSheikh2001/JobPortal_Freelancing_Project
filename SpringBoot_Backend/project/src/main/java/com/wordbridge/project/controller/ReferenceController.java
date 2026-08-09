package com.wordbridge.project.controller;

import com.wordbridge.project.dto.requestdto.ReferenceRequestDTO;
import com.wordbridge.project.dto.responsedto.ReferenceResponseDTO;
import com.wordbridge.project.dto.responsedto.UserProfileResponseDTO;
import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.UserRole;
import com.wordbridge.project.security.AuthenticationService;
import com.wordbridge.project.service.ReferenceService;
import com.wordbridge.project.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/references/")
@RequiredArgsConstructor
public class ReferenceController {
    private final ReferenceService referenceService;

    private final UserProfileService userProfileService;
    private final AuthenticationService authenticationService;


    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<ReferenceResponseDTO> save(@RequestBody ReferenceRequestDTO dto) {
        checkProfileOwnership(dto.getUserProfileId());
        ReferenceResponseDTO saved = referenceService.save(dto);
        return ResponseEntity.ok(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ReferenceResponseDTO>> getAll() {
        List<ReferenceResponseDTO> list = referenceService.getAll();
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("{id}")
    public ResponseEntity<ReferenceResponseDTO> getById(@PathVariable Long id) {
        checkRecordOwnership(id);
        ReferenceResponseDTO r = referenceService.findById(id);
        return ResponseEntity.ok(r);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("{id}")
    public ResponseEntity<ReferenceResponseDTO> update(@RequestBody ReferenceRequestDTO ul, @PathVariable Long id) {

        checkRecordOwnership(id);
        ReferenceResponseDTO updated = referenceService.update(id, ul);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        checkRecordOwnership(id);
        referenceService.delete(id);
        return ResponseEntity.ok("User Reference Deleted");
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("userprofile/{id}")
    public List<ReferenceResponseDTO> findByUserProfileId(@PathVariable Long id) {
        return referenceService.findByUserProfileId(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("userprofile/count/{id}")
    public Long countByUserProfileId(@PathVariable Long id) {
        return referenceService.countByUserProfileId(id);
    }



    private void checkRecordOwnership(Long referenceId) {
        User currentUser = authenticationService.getCurrentUser();
        ReferenceResponseDTO existing = referenceService.findById(referenceId);
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
