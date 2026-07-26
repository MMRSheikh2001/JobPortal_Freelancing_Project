package com.wordbridge.project.controller;

import com.wordbridge.project.dto.responsedto.ResumeResponseDTO;
import com.wordbridge.project.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/resume/")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    // Existing — returns JSON
    @GetMapping("{userProfileId}")
    public ResponseEntity<ResumeResponseDTO> generateResume(
            @PathVariable Long userProfileId) {
        return ResponseEntity.ok(resumeService.generateResume(userProfileId));
    }

    // Preview — returns rendered HTML string
    // Angular can display this in an <iframe srcdoc="..."> or new tab
    @GetMapping(value = "{userProfileId}/html",
            produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> generateHtml(
            @PathVariable Long userProfileId) {
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(resumeService.generateHtml(userProfileId));
    }

    // Download — returns PDF bytes as file download
    @GetMapping("{userProfileId}/pdf")
    public ResponseEntity<byte[]> generatePdf(
            @PathVariable Long userProfileId) {
        byte[] pdf = resumeService.generatePdf(userProfileId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"cv.pdf\"")
                .body(pdf);
    }
}