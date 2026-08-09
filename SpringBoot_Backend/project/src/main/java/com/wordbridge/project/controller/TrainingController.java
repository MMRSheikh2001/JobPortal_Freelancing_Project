package com.wordbridge.project.controller;

import com.wordbridge.project.dto.requestdto.TrainingRequestDTO;
import com.wordbridge.project.dto.responsedto.TrainingResponseDTO;
import com.wordbridge.project.dto.responsedto.UserProfileResponseDTO;
import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.UserRole;
import com.wordbridge.project.security.AuthenticationService;
import com.wordbridge.project.service.TrainingService;
import com.wordbridge.project.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/trainings/")
@RequiredArgsConstructor
public class TrainingController {
    private final TrainingService trainingService;

    private final UserProfileService userProfileService;
    private final AuthenticationService authenticationService;


    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<TrainingResponseDTO> save(
            @RequestPart("training") TrainingRequestDTO dto,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        checkProfileOwnership(dto.getUserProfileId());
        return new ResponseEntity<>(
                trainingService.save(dto, file),
                HttpStatus.CREATED
        );
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<TrainingResponseDTO>> getAll() {
        List<TrainingResponseDTO> list = trainingService.getAll();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("{id}")
    public ResponseEntity<TrainingResponseDTO> getById(@PathVariable Long id) {
        checkRecordOwnership(id);
        TrainingResponseDTO dto = trainingService.findById(id);

        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("{id}")
    public ResponseEntity<TrainingResponseDTO> update(@RequestPart("training") TrainingRequestDTO dto,
                                                      @RequestPart(value = "file", required = false) MultipartFile file,
                                                      @PathVariable Long id) {

        checkRecordOwnership(id);
        TrainingResponseDTO updated = trainingService.update(id, dto, file);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        checkRecordOwnership(id);
        trainingService.delete(id);
        return ResponseEntity.ok("Training Deleted");
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("{id}/file")
    public ResponseEntity<String> deleteFile(@PathVariable Long id) {
        checkRecordOwnership(id);

        trainingService.deleteFile(id);

        return ResponseEntity.ok(
                "Training File successfully"
        );
    }

    //Find Trainings By User Profile id
    @PreAuthorize("isAuthenticated()")
    @GetMapping("userprofile/{userProfileId}")
    public ResponseEntity<List<TrainingResponseDTO>> getByUserProfileId(
            @PathVariable Long userProfileId) {
        List<TrainingResponseDTO> list = trainingService.findByUserProfileId(userProfileId);

        return ResponseEntity.ok(list);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("count/userprofile/{userProfileId}")
    public ResponseEntity<Long> countByUserProfileId(
            @PathVariable Long userProfileId) {

        return ResponseEntity.ok(
                trainingService.countByUserProfileId(userProfileId)
        );
    }


    private void checkRecordOwnership(Long trainingId) {
        User currentUser = authenticationService.getCurrentUser();
        TrainingResponseDTO existing = trainingService.findById(trainingId);
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
