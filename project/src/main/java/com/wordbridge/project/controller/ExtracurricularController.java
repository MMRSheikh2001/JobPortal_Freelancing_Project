package com.wordbridge.project.controller;

import com.wordbridge.project.dto.requestdto.ExtracurricularRequestDTO;
import com.wordbridge.project.dto.responsedto.ExtracurricularResponseDTO;
import com.wordbridge.project.service.ExtracurricularService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/extracurriculars/")
@RequiredArgsConstructor
public class ExtracurricularController {

    private final ExtracurricularService extracurricularService;


    @PostMapping
    public ResponseEntity<ExtracurricularResponseDTO> save(@RequestBody ExtracurricularRequestDTO dto) {
        ExtracurricularResponseDTO saved = extracurricularService.save(dto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<ExtracurricularResponseDTO>> getAll() {
        List<ExtracurricularResponseDTO> list = extracurricularService.getAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("{id}")
    public ResponseEntity<ExtracurricularResponseDTO> getById(@PathVariable Long id) {
        ExtracurricularResponseDTO e = extracurricularService.findById(id);
        return ResponseEntity.ok(e);
    }

    @PutMapping("{id}")
    public ResponseEntity<ExtracurricularResponseDTO> update(@RequestBody ExtracurricularRequestDTO dto, @PathVariable Long id) {

        ExtracurricularResponseDTO updated = extracurricularService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        extracurricularService.delete(id);
        return ResponseEntity.ok("User Extracurricular Deleted");
    }


    @GetMapping("userprofile/{id}")
    public List<ExtracurricularResponseDTO> findByUserProfileId(@PathVariable Long id) {
        return extracurricularService.findByUserProfileId(id);
    }

    @GetMapping("userprofile/count/{id}")
    public Long countByUserProfileId(@PathVariable Long id) {
        return extracurricularService.countByUserProfileId(id);
    }


}
