package com.wordbridge.project.controller;

import com.wordbridge.project.dto.requestdto.ExperienceRequestDTO;
import com.wordbridge.project.dto.responsedto.ExperienceResponseDTO;
import com.wordbridge.project.service.ExperienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/experiences/")
@RequiredArgsConstructor
public class ExperienceController {
    private final ExperienceService experienceService;


    @PostMapping
    public ResponseEntity<ExperienceResponseDTO> save(@RequestBody ExperienceRequestDTO dto) {
        ExperienceResponseDTO saved = experienceService.save(dto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<ExperienceResponseDTO>> getAll() {
        List<ExperienceResponseDTO> list = experienceService.getAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("{id}")
    public ResponseEntity<ExperienceResponseDTO> getById(@PathVariable Long id) {
        ExperienceResponseDTO ul = experienceService.findById(id);
        return ResponseEntity.ok(ul);
    }

    @PutMapping("{id}")
    public ResponseEntity<ExperienceResponseDTO> update(@RequestBody ExperienceRequestDTO dto, @PathVariable Long id) {

        ExperienceResponseDTO updated = experienceService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        experienceService.delete(id);
        return ResponseEntity.ok("User Experience Deleted");
    }


    @GetMapping("userprofile/{id}")
    public List<ExperienceResponseDTO> findByUserProfileId(@PathVariable Long id) {
        return experienceService.findByUserProfileId(id);
    }

    @GetMapping("userprofile/count/{id}")
    public Long countByUserProfileId(@PathVariable Long id) {
        return experienceService.countByUserProfileId(id);
    }


}
