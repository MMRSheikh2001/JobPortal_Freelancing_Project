package com.wordbridge.project.controller;

import com.wordbridge.project.dto.requestdto.ExtracurricularRequestDTO;
import com.wordbridge.project.dto.responsedto.ExtracurricularResponseDTO;
import com.wordbridge.project.dto.responsedto.UserProfileResponseDTO;
import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.UserRole;
import com.wordbridge.project.security.AuthenticationService;
import com.wordbridge.project.service.ExtracurricularService;
import com.wordbridge.project.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/extracurriculars/")
@RequiredArgsConstructor
public class ExtracurricularController {

    private final ExtracurricularService extracurricularService;

    private final UserProfileService userProfileService;
    private final AuthenticationService authenticationService;


    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<ExtracurricularResponseDTO> save(@RequestBody ExtracurricularRequestDTO dto) {
        checkProfileOwnership(dto.getUserProfileId());
        ExtracurricularResponseDTO saved = extracurricularService.save(dto);
        return ResponseEntity.ok(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ExtracurricularResponseDTO>> getAll() {
        List<ExtracurricularResponseDTO> list = extracurricularService.getAll();
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("{id}")
    public ResponseEntity<ExtracurricularResponseDTO> getById(@PathVariable Long id) {
        checkRecordOwnership(id);
        ExtracurricularResponseDTO e = extracurricularService.findById(id);
        return ResponseEntity.ok(e);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("{id}")
    public ResponseEntity<ExtracurricularResponseDTO> update(@RequestBody ExtracurricularRequestDTO dto, @PathVariable Long id) {

        checkRecordOwnership(id);
        ExtracurricularResponseDTO updated = extracurricularService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        checkRecordOwnership(id);
        extracurricularService.delete(id);
        return ResponseEntity.ok("User Extracurricular Deleted");
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("userprofile/{id}")
    public List<ExtracurricularResponseDTO> findByUserProfileId(@PathVariable Long id) {
        return extracurricularService.findByUserProfileId(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("userprofile/count/{id}")
    public Long countByUserProfileId(@PathVariable Long id) {
        return extracurricularService.countByUserProfileId(id);
    }


    private void checkRecordOwnership(Long extracurricularId) {
        User currentUser = authenticationService.getCurrentUser();
        ExtracurricularResponseDTO existing = extracurricularService.findById(extracurricularId);
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
