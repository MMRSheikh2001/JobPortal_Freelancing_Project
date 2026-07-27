package com.wordbridge.project.controller;


import com.wordbridge.project.dto.requestdto.UserSkillRequestDTO;
import com.wordbridge.project.dto.responsedto.UserProfileResponseDTO;
import com.wordbridge.project.dto.responsedto.UserSkillResponseDTO;
import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.UserRole;
import com.wordbridge.project.security.AuthenticationService;
import com.wordbridge.project.service.UserProfileService;
import com.wordbridge.project.service.UserSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/userskills/")
@RequiredArgsConstructor
public class UserSkillController {

    private final UserSkillService userSkillService;

    private final UserProfileService userProfileService;
    private final AuthenticationService authenticationService;


    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<UserSkillResponseDTO> save(@RequestBody UserSkillRequestDTO us) {
        checkProfileOwnership(us.getUserProfileId());
        UserSkillResponseDTO savedUserSkill = userSkillService.save(us);
        return ResponseEntity.ok(savedUserSkill);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserSkillResponseDTO>> getAll() {
        List<UserSkillResponseDTO> list = userSkillService.getAll();
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("{id}")
    public ResponseEntity<UserSkillResponseDTO> getById(@PathVariable Long id) {
        checkRecordOwnership(id);
        UserSkillResponseDTO us = userSkillService.findById(id);
        return ResponseEntity.ok(us);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("{id}")
    public ResponseEntity<UserSkillResponseDTO> update(@RequestBody UserSkillRequestDTO us, @PathVariable Long id) {

        checkRecordOwnership(id);
        UserSkillResponseDTO updatedUserSkill = userSkillService.update(id, us);
        return ResponseEntity.ok(updatedUserSkill);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        checkRecordOwnership(id);
        userSkillService.delete(id);
        return ResponseEntity.ok("User Skill Deleted");
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("userprofile/{id}")
    public List<UserSkillResponseDTO> findByUserProfileId(@PathVariable Long id) {
        return userSkillService.findByUserProfileId(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("skill/{id}")
    public List<UserSkillResponseDTO> findBySkillId(@PathVariable Long id) {
        return userSkillService.findBySkillId(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("skill/category/{id}")
    public List<UserSkillResponseDTO> findBySkillCategoryId(@PathVariable Long id) {
        return userSkillService.findBySkillCategoryId(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("userprofile/{userProfileId}/skill/{skillId}")
    public ResponseEntity<UserSkillResponseDTO> findByUserProfileIdAndSkillId(
            @PathVariable Long userProfileId,
            @PathVariable Long skillId) {

        UserSkillResponseDTO dto = userSkillService
                .findByUserProfileIdAndSkillId(userProfileId, skillId);


        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("userprofile/{userProfileId}/skill/{skillId}")
    public ResponseEntity<String> deleteByUserProfileIdAndSkillId(@PathVariable Long userProfileId, @PathVariable Long skillId) {

        checkProfileOwnership(userProfileId);
        userSkillService.deleteByUserProfileIdAndSkillId(userProfileId, skillId);
        return ResponseEntity.ok("User Skill Deleted");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("userprofile/count/{userProfileId}")
    public ResponseEntity<Long> countSkillsByUserProfileId(@PathVariable Long userProfileId) {
        return ResponseEntity.ok(userSkillService.countSkillsByUserProfileId(userProfileId));
    }


    private void checkRecordOwnership(Long userSkillId) {
        User currentUser = authenticationService.getCurrentUser();
        UserSkillResponseDTO existing = userSkillService.findById(userSkillId);
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
