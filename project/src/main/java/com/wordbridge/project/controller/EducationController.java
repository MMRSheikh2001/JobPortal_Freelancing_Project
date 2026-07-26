package com.wordbridge.project.controller;

import com.wordbridge.project.dto.requestdto.EducationRequestDTO;
import com.wordbridge.project.dto.responsedto.EducationResponseDTO;
import com.wordbridge.project.service.EducationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/educations/")
@RequiredArgsConstructor
public class EducationController {
    private final EducationService educationService;


    @PostMapping
    public ResponseEntity<EducationResponseDTO> save(@RequestBody EducationRequestDTO dto) {
        EducationResponseDTO saved = educationService.save(dto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<EducationResponseDTO>> getAll() {
        List<EducationResponseDTO> list = educationService.getAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("{id}")
    public ResponseEntity<EducationResponseDTO> getById(@PathVariable Long id) {
        EducationResponseDTO ul = educationService.findById(id);
        return ResponseEntity.ok(ul);
    }

    @PutMapping("{id}")
    public ResponseEntity<EducationResponseDTO> update(@RequestBody EducationRequestDTO ul, @PathVariable Long id) {

        EducationResponseDTO updated = educationService.update(id, ul);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        educationService.delete(id);
        return ResponseEntity.ok("User Education Deleted");
    }


    @GetMapping("userprofile/{id}")
    public List<EducationResponseDTO> findByUserProfileId(@PathVariable Long id) {
        return educationService.findByUserProfileId(id);
    }

    @GetMapping("userprofile/count/{id}")
    public Long countByUserProfileId(@PathVariable Long id) {
        return educationService.countByUserProfileId(id);
    }


}
