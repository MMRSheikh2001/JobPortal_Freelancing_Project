package com.wordbridge.project.controller;

import com.wordbridge.project.dto.requestdto.ReferenceRequestDTO;
import com.wordbridge.project.dto.responsedto.ReferenceResponseDTO;
import com.wordbridge.project.service.ReferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/references/")
@RequiredArgsConstructor
public class ReferenceController {
    private final ReferenceService referenceService;


    @PostMapping
    public ResponseEntity<ReferenceResponseDTO> save(@RequestBody ReferenceRequestDTO dto) {
        ReferenceResponseDTO saved = referenceService.save(dto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<ReferenceResponseDTO>> getAll() {
        List<ReferenceResponseDTO> list = referenceService.getAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("{id}")
    public ResponseEntity<ReferenceResponseDTO> getById(@PathVariable Long id) {
        ReferenceResponseDTO r = referenceService.findById(id);
        return ResponseEntity.ok(r);
    }

    @PutMapping("{id}")
    public ResponseEntity<ReferenceResponseDTO> update(@RequestBody ReferenceRequestDTO ul, @PathVariable Long id) {

        ReferenceResponseDTO updated = referenceService.update(id, ul);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        referenceService.delete(id);
        return ResponseEntity.ok("User Reference Deleted");
    }


    @GetMapping("userprofile/{id}")
    public List<ReferenceResponseDTO> findByUserProfileId(@PathVariable Long id) {
        return referenceService.findByUserProfileId(id);
    }

    @GetMapping("userprofile/count/{id}")
    public Long countByUserProfileId(@PathVariable Long id) {
        return referenceService.countByUserProfileId(id);
    }


}
