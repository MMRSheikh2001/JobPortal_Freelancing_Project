package com.wordbridge.project.controller;

import com.wordbridge.project.dto.requestdto.UserLanguageRequestDTO;
import com.wordbridge.project.dto.responsedto.UserLanguageResponseDTO;
import com.wordbridge.project.dto.responsedto.UserProfileResponseDTO;
import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.UserRole;
import com.wordbridge.project.security.AuthenticationService;
import com.wordbridge.project.service.UserLanguageService;
import com.wordbridge.project.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/userlanguages/")
@RequiredArgsConstructor
public class UserLanguageController {

    private final UserLanguageService userLanguageService;

    private final UserProfileService userProfileService;
    private final AuthenticationService authenticationService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<UserLanguageResponseDTO> save(@RequestBody UserLanguageRequestDTO ul) {
        checkProfileOwnership(ul.getUserProfileId());
        UserLanguageResponseDTO saved = userLanguageService.save(ul);
        return ResponseEntity.ok(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserLanguageResponseDTO>> getAll() {
        List<UserLanguageResponseDTO> list = userLanguageService.getAll();
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("{id}")
    public ResponseEntity<UserLanguageResponseDTO> getById(@PathVariable Long id) {
        checkRecordOwnership(id);
        UserLanguageResponseDTO ul = userLanguageService.findById(id);
        return ResponseEntity.ok(ul);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("{id}")
    public ResponseEntity<UserLanguageResponseDTO> update(@RequestBody UserLanguageRequestDTO ul, @PathVariable Long id) {

        checkRecordOwnership(id);
        UserLanguageResponseDTO updated = userLanguageService.update(id, ul);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        checkRecordOwnership(id);
        userLanguageService.delete(id);
        return ResponseEntity.ok("User Language Deleted");
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("userprofile/{id}")
    public List<UserLanguageResponseDTO> findByUserProfileId(@PathVariable Long id) {
        return userLanguageService.findByUserProfileId(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("language/{id}")
    public List<UserLanguageResponseDTO> findByLanguageId(@PathVariable Long id) {
        return userLanguageService.findByLanguageId(id);
    }


    @GetMapping("userprofile/{userProfileId}/language/{languageId}")
    public ResponseEntity<UserLanguageResponseDTO> findByUserProfileIdAndLanguageId(
            @PathVariable Long userProfileId,
            @PathVariable Long languageId) {

        UserLanguageResponseDTO dto = userLanguageService
                .findByUserProfileIdAndLanguageId(userProfileId, languageId);


        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("userprofile/{userProfileId}/language/{languageId}")
    public ResponseEntity<String> deleteByUserProfileIdAndLanguageId(@PathVariable Long userProfileId, @PathVariable Long languageId) {

        checkProfileOwnership(userProfileId);
        userLanguageService.deleteByUserProfileIdAndLanguageId(userProfileId, languageId);
        return ResponseEntity.ok("User Language Deleted");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("userprofile/count/{userProfileId}")
    public ResponseEntity<Long> countLanguagesByUserProfileId(@PathVariable Long userProfileId) {
        return ResponseEntity.ok(userLanguageService.countLanguagesByUserProfileId(userProfileId));
    }


    private void checkRecordOwnership(Long userLanguageId) {
        User currentUser = authenticationService.getCurrentUser();
        UserLanguageResponseDTO existing = userLanguageService.findById(userLanguageId);
        if (!existing.getUserProfileId().equals(currentUser.getUserProfile().getId()) && currentUser.getRole() != UserRole.ADMIN) {
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
