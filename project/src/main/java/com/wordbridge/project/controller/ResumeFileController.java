package com.wordbridge.project.controller;

import com.wordbridge.project.dto.requestdto.ResumeRequestDTO;
import com.wordbridge.project.dto.responsedto.ResumeFileResponseDTO;
import com.wordbridge.project.service.ResumeFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/resumes/uploadedfile/")
@RequiredArgsConstructor
public class ResumeFileController {
    private final ResumeFileService resumeFileService;

    @PostMapping
    public ResponseEntity<ResumeFileResponseDTO> uploadResume(
            @RequestParam Long userProfileId,
            @RequestParam MultipartFile cv) {

        ResumeRequestDTO dto = new ResumeRequestDTO();
        dto.setUserProfileId(userProfileId);

        ResumeFileResponseDTO response = resumeFileService.save(dto, cv);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public List<ResumeFileResponseDTO> findAll() {
        return resumeFileService.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<ResumeFileResponseDTO> getById(@PathVariable Long id) {
        ResumeFileResponseDTO dto = resumeFileService.getById(id);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        resumeFileService.delete(id);
        return ResponseEntity.ok("Resume deleted");
    }

    @GetMapping("/user/{userProfileId}")
    public ResumeFileResponseDTO findByUserProfileId(@PathVariable Long userProfileId) {
        return resumeFileService.findByUserProfileId(userProfileId);
    }


    @DeleteMapping("/user/{userProfileId}")
    public ResponseEntity<String> deleteByUserProfileId(@PathVariable Long userProfileId) {
        resumeFileService.deleteByUserProfileId(userProfileId);
        return ResponseEntity.ok("Resume deleted successfully");
    }

    @GetMapping("/exists/{userProfileId}")
    public ResponseEntity<Boolean> exists(@PathVariable Long userProfileId) {
        return ResponseEntity.ok(resumeFileService.existsByUserProfileId(userProfileId));
    }


}
