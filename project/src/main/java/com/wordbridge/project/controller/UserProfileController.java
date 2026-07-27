package com.wordbridge.project.controller;

import com.wordbridge.project.dto.requestdto.UserProfileRequestDTO;
import com.wordbridge.project.dto.responsedto.UserProfileResponseDTO;
import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.JobType;
import com.wordbridge.project.enums.UserRole;
import com.wordbridge.project.enums.WorkPlaceType;
import com.wordbridge.project.security.AuthenticationService;
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
@RequestMapping("/api/userprofiles/")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final AuthenticationService authenticationService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<UserProfileResponseDTO> save(
            @RequestPart("userprofile") UserProfileRequestDTO up,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        User currentUser = authenticationService.getCurrentUser();   // NEW
        up.setUserId(currentUser.getId());
        return new ResponseEntity<>(
                userProfileService.save(up, image),
                HttpStatus.CREATED
        );
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('COMPANY')")
    @GetMapping
    public ResponseEntity<List<UserProfileResponseDTO>> getAll() {
        List<UserProfileResponseDTO> list = userProfileService.getAll();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("{id}")
    public ResponseEntity<UserProfileResponseDTO> getById(@PathVariable Long id) {
        UserProfileResponseDTO up = userProfileService.findById(id);

        return ResponseEntity.ok(up);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("{id}")
    public ResponseEntity<UserProfileResponseDTO> update(@RequestPart("userprofile") UserProfileRequestDTO up,
                                                         @RequestPart(value = "image", required = false) MultipartFile image,
                                                         @PathVariable Long id) {

        checkOwnership(id);
        UserProfileResponseDTO updatedUserProfile = userProfileService.update(id, up, image);
        return ResponseEntity.ok(updatedUserProfile);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        checkOwnership(id);
        userProfileService.delete(id);
        return ResponseEntity.ok("User Profile Deleted");
    }


    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("{id}/image")
    public ResponseEntity<String> deleteImage(@PathVariable Long id) {
        checkOwnership(id);
        userProfileService.deleteImage(id);

        return ResponseEntity.ok(
                "Profile image deleted successfully"
        );
    }

    //Find User Profile By User id
    @PreAuthorize("permitAll()")
    @GetMapping("user/{userId}")
    public ResponseEntity<UserProfileResponseDTO> getByUserId(
            @PathVariable Long userId) {
        UserProfileResponseDTO up = userProfileService.findByUserId(userId);

        return ResponseEntity.ok(up);
    }


    //Find By Address

    @PreAuthorize("hasRole('ADMIN') or hasRole('COMPANY')")
    @GetMapping("presentaddress/policestation/{id}")
    public List<UserProfileResponseDTO> findByPresentAddressPoliceStationId(@PathVariable Long id) {
        return userProfileService.findByPresentAddressPoliceStationId(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('COMPANY')")
    @GetMapping("presentaddress/policestation/district/{id}")
    public List<UserProfileResponseDTO> findByPresentAddressPoliceStationDistrictId(@PathVariable Long id) {
        return userProfileService.findByPresentAddressPoliceStationDistrictId(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('COMPANY')")
    @GetMapping("permanentaddress/policestation/{id}")
    public List<UserProfileResponseDTO> findByPermanentAddressPoliceStationId(@PathVariable Long id) {
        return userProfileService.findByPermanentAddressPoliceStationId(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('COMPANY')")
    @GetMapping("permanentaddress/policestation/district/{id}")
    public List<UserProfileResponseDTO> findByPermanentAddressPoliceStationDistrictId(@PathVariable Long id) {
        return userProfileService.findByPermanentAddressPoliceStationDistrictId(id);
    }


    // ==========================
// Filter User Profiles
// ==========================
    @PreAuthorize("hasRole('ADMIN') or hasRole('COMPANY')")
    @GetMapping("filter")
    public ResponseEntity<List<UserProfileResponseDTO>> filterUsers(

            @RequestParam(required = false) String keyword,

            @RequestParam(required = false) Long countryId,

            @RequestParam(required = false) Long divisionId,

            @RequestParam(required = false) Long districtId,

            @RequestParam(required = false) Long policeStationId,

            @RequestParam(required = false) JobType jobType,

            @RequestParam(required = false) WorkPlaceType workPlaceType,

            @RequestParam(required = false) String gender

    ) {

        return ResponseEntity.ok(

                userProfileService.filterUsers(

                        keyword,

                        countryId,

                        divisionId,

                        districtId,

                        policeStationId,

                        jobType,

                        workPlaceType,

                        gender

                )

        );
    }

    private void checkOwnership(Long profileId) {
        User currentUser = authenticationService.getCurrentUser();
        UserProfileResponseDTO existing = userProfileService.findById(profileId);
        if (!existing.getUserId().equals(currentUser.getId()) && currentUser.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Not allowed");
        }
    }


}
