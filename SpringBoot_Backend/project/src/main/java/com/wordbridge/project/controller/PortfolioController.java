package com.wordbridge.project.controller;

import com.wordbridge.project.dto.requestdto.PortfolioRequestDTO;
import com.wordbridge.project.dto.responsedto.PortfolioResponseDTO;
import com.wordbridge.project.dto.responsedto.UserProfileResponseDTO;
import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.UserRole;
import com.wordbridge.project.security.AuthenticationService;
import com.wordbridge.project.service.PortfolioService;
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
@RequestMapping("/api/portfolios/")
@RequiredArgsConstructor
public class PortfolioController {
    private final PortfolioService portfolioService;

    private final UserProfileService userProfileService;
    private final AuthenticationService authenticationService;


    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<PortfolioResponseDTO> save(
            @RequestPart("portfolio") PortfolioRequestDTO dto,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        checkProfileOwnership(dto.getUserProfileId());
        return new ResponseEntity<>(
                portfolioService.save(dto, file),
                HttpStatus.CREATED
        );
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<PortfolioResponseDTO>> getAll() {
        List<PortfolioResponseDTO> list = portfolioService.getAll();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("{id}")
    public ResponseEntity<PortfolioResponseDTO> getById(@PathVariable Long id) {
        checkRecordOwnership(id);
        PortfolioResponseDTO dto = portfolioService.findById(id);

        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("{id}")
    public ResponseEntity<PortfolioResponseDTO> update(@RequestPart("portfolio") PortfolioRequestDTO dto,
                                                       @RequestPart(value = "file", required = false) MultipartFile file,
                                                       @PathVariable Long id) {

        checkRecordOwnership(id);
        PortfolioResponseDTO updated = portfolioService.update(id, dto, file);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        checkRecordOwnership(id);
        portfolioService.delete(id);
        return ResponseEntity.ok("Portfolio  Deleted");
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("{id}/file")
    public ResponseEntity<String> deleteFile(@PathVariable Long id) {

        checkRecordOwnership(id);
        portfolioService.deleteFile(id);

        return ResponseEntity.ok(
                "Portfolio File successfully deleted"
        );
    }

    //Find Portfolio By User Profile id
    @PreAuthorize("isAuthenticated()")
    @GetMapping("userprofile/{userProfileId}")
    public ResponseEntity<List<PortfolioResponseDTO>> getByUserProfileId(
            @PathVariable Long userProfileId) {
        List<PortfolioResponseDTO> list = portfolioService.findByUserProfileId(userProfileId);

        return ResponseEntity.ok(list);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("count/userprofile/{userProfileId}")
    public ResponseEntity<Long> countByUserProfileId(
            @PathVariable Long userProfileId) {

        return ResponseEntity.ok(
                portfolioService.countByUserProfileId(userProfileId)
        );
    }


    private void checkRecordOwnership(Long portfolioId) {
        User currentUser = authenticationService.getCurrentUser();
        PortfolioResponseDTO existing = portfolioService.findById(portfolioId);
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
