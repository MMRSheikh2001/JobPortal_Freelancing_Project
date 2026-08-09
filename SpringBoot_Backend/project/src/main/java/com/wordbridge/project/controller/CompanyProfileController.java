package com.wordbridge.project.controller;

import com.wordbridge.project.dto.requestdto.CompanyProfileRequestDTO;
import com.wordbridge.project.dto.responsedto.CompanyProfileResponseDTO;
import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.UserRole;
import com.wordbridge.project.job.CompanySearchRequestDTO;
import com.wordbridge.project.security.AuthenticationService;
import com.wordbridge.project.service.CompanyProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/companies/")
@RequiredArgsConstructor
public class CompanyProfileController {

    private final CompanyProfileService companyProfileService;
    private final AuthenticationService authenticationService;


    @PreAuthorize("hasRole('COMPANY')")
    @PostMapping
    public ResponseEntity<CompanyProfileResponseDTO> save(
            @RequestPart("companyprofile") CompanyProfileRequestDTO cp,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {

        User currentUser = authenticationService.getCurrentUser();   // NEW
        cp.setUserId(currentUser.getId());
        return new ResponseEntity<>(
                companyProfileService.save(cp, image),
                HttpStatus.CREATED
        );
    }


    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<List<CompanyProfileResponseDTO>> getAll() {
        List<CompanyProfileResponseDTO> list = companyProfileService.getAll();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("{id}")
    public ResponseEntity<CompanyProfileResponseDTO> getById(@PathVariable Long id) {
        CompanyProfileResponseDTO cp = companyProfileService.findById(id);

        return ResponseEntity.ok(cp);
    }

    @PreAuthorize("hasRole('COMPANY')")
    @PutMapping("{id}")
    public ResponseEntity<CompanyProfileResponseDTO> update(@RequestPart("companyprofile") CompanyProfileRequestDTO cp,
                                                            @RequestPart(value = "image", required = false) MultipartFile image,
                                                            @PathVariable Long id) {

        checkOwnership(id);
        CompanyProfileResponseDTO updated = companyProfileService.update(id, cp, image);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('COMPANY')")
    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        checkOwnership(id);
        companyProfileService.delete(id);
        return ResponseEntity.ok("Company Profile Deleted");
    }

    @PreAuthorize("hasRole('COMPANY')")
    @DeleteMapping("{id}/image")
    public ResponseEntity<String> deleteImage(@PathVariable Long id) {
        checkOwnership(id);

        companyProfileService.deleteImage(id);

        return ResponseEntity.ok(
                "Profile image deleted successfully"
        );
    }

    //Find User Profile By User Id
    @PreAuthorize("permitAll()")
    @GetMapping("user/{userId}")
    public ResponseEntity<CompanyProfileResponseDTO> getByUserId(
            @PathVariable Long userId) {
        CompanyProfileResponseDTO cp = companyProfileService.findByUserId(userId);
        return ResponseEntity.ok(cp);
    }

    //Find By address


    @PreAuthorize("permitAll()")
    @GetMapping("location/policestation/{id}")
    public List<CompanyProfileResponseDTO> findByLocationPoliceStationId(@PathVariable Long id) {
        return companyProfileService.findByLocationPoliceStationId(id);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("location/policestation/district/{id}")
    public List<CompanyProfileResponseDTO> findByLocationPoliceStationDistrictId(@PathVariable Long id) {
        return companyProfileService.findByLocationPoliceStationDistrictId(id);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("exists/user/{userId}")
    public ResponseEntity<Boolean> existsByUserId(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                companyProfileService.existsByUserId(userId)
        );
    }

    @PreAuthorize("permitAll()")
    @GetMapping("count")
    public ResponseEntity<Long> count() {

        return ResponseEntity.ok(
                companyProfileService.count()
        );
    }

    //Find by Keyword
    @PreAuthorize("permitAll()")
    @GetMapping("search")
    public List<CompanyProfileResponseDTO> search(
            @RequestParam String keyword) {

        return companyProfileService.search(keyword);
    }

    @PostMapping("filter")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<CompanyProfileResponseDTO>> search(
            @RequestBody CompanySearchRequestDTO request
    ){
        System.out.println("===== FILTER ENDPOINT HIT =====");

        return ResponseEntity.ok(
                companyProfileService.search(request)
        );

    }

    private void checkOwnership(Long companyProfileId) {
        User currentUser = authenticationService.getCurrentUser();
        CompanyProfileResponseDTO existing = companyProfileService.findById(companyProfileId);
        if (!existing.getUserId().equals(currentUser.getId()) && currentUser.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Not allowed");
        }
    }

}
