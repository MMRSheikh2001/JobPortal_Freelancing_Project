package com.wordbridge.project.report;

import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.ReportStatus;
import com.wordbridge.project.enums.ReportType;
import com.wordbridge.project.enums.UserRole;
import com.wordbridge.project.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    private final AuthenticationService authenticationService;

    //--------------------------------
    // Create Report
    //--------------------------------

    @PostMapping
    public ResponseEntity<ReportResponseDTO> createReport(

            @RequestParam Long userId,

            @RequestParam String subject,

            @RequestParam String description,

            @RequestParam ReportType type,

            @RequestPart(required = false) MultipartFile attachment

    ) {

        User currentUser = authenticationService.getCurrentUser();

        if (!currentUser.getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        return ResponseEntity.ok(

                reportService.createReport(
                        userId,
                        subject,
                        description,
                        type,
                        attachment
                )

        );

    }

    //--------------------------------
    // Resolve
    //--------------------------------

    @PatchMapping("/{reportId}/resolve")
    public ResponseEntity<ReportResponseDTO> resolveReport(

            @PathVariable Long reportId,

            @RequestParam String adminReply

    ) {
        User currentUser = authenticationService.getCurrentUser();

        if (!currentUser.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("Unauthorized");
        }

        return ResponseEntity.ok(

                reportService.resolveReport(
                        reportId,
                        adminReply
                )

        );

    }

    //--------------------------------
    // Reject
    //--------------------------------

    @PatchMapping("/{reportId}/reject")
    public ResponseEntity<ReportResponseDTO> rejectReport(

            @PathVariable Long reportId,

            @RequestParam String adminReply

    ) {
        User currentUser = authenticationService.getCurrentUser();

        if (!currentUser.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("Unauthorized");
        }

        return ResponseEntity.ok(

                reportService.rejectReport(
                        reportId,
                        adminReply
                )

        );

    }

    //--------------------------------
    // Get All
    //--------------------------------

    @GetMapping
    public ResponseEntity<List<ReportResponseDTO>> getAll() {

        User currentUser = authenticationService.getCurrentUser();

        if (!currentUser.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("Unauthorized");
        }


        return ResponseEntity.ok(
                reportService.getAll()
        );

    }

    //--------------------------------
    // Get By Id
    //--------------------------------

    @GetMapping("/{reportId}")
    public ResponseEntity<ReportResponseDTO> getById(

            @PathVariable Long reportId

    ) {

        User currentUser = authenticationService.getCurrentUser();

        if (!currentUser.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("Unauthorized");
        }

        return ResponseEntity.ok(

                reportService.getById(reportId)

        );

    }

    //--------------------------------
    // Get By User
    //--------------------------------

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReportResponseDTO>> getByUserId(


            @PathVariable Long userId

    ) {

        User currentUser = authenticationService.getCurrentUser();

        if (!currentUser.getId().equals(userId)) {

            throw new RuntimeException("Unauthorized");
        }

        return ResponseEntity.ok(

                reportService.getByUserId(userId)

        );

    }

    //--------------------------------
    // Get By Status
    //--------------------------------

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ReportResponseDTO>> getByStatus(

            @PathVariable ReportStatus status

    ) {

        User currentUser = authenticationService.getCurrentUser();

        if (!currentUser.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("Unauthorized");
        }

        return ResponseEntity.ok(

                reportService.getByStatus(status)

        );

    }

    //--------------------------------
    // Count By Status
    //--------------------------------

    @GetMapping("/status/{status}/count")
    public ResponseEntity<Long> countByStatus(

            @PathVariable ReportStatus status

    ) {
        User currentUser = authenticationService.getCurrentUser();

        if (!currentUser.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("Unauthorized");
        }

        return ResponseEntity.ok(

                reportService.countByStatus(status)

        );

    }

    //--------------------------------
    // Search
    //--------------------------------

    @PostMapping("/search")
    public List<ReportResponseDTO> search(

            @RequestBody ReportFilterRequestDTO dto

    ) {

        User currentUser = authenticationService.getCurrentUser();

        if (!currentUser.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("Unauthorized");
        }

        return reportService.search(dto);

    }

}
