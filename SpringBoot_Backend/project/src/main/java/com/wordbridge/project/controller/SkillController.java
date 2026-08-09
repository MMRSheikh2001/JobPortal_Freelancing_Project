package com.wordbridge.project.controller;


import com.wordbridge.project.dto.requestdto.SkillRequestDTO;
import com.wordbridge.project.dto.responsedto.SkillResponseDTO;
import com.wordbridge.project.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills/")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<SkillResponseDTO> save(@RequestBody SkillRequestDTO s) {
        SkillResponseDTO savedSkill = skillService.save(s);
        return ResponseEntity.ok(savedSkill);
    }


    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<List<SkillResponseDTO>> getAll() {
        List<SkillResponseDTO> list = skillService.getAll();
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("{id}")
    public ResponseEntity<SkillResponseDTO> getById(@PathVariable Long id) {
        SkillResponseDTO skill = skillService.findById(id);
        return ResponseEntity.ok(skill);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<SkillResponseDTO> update(@RequestBody SkillRequestDTO s, @PathVariable Long id) {

        SkillResponseDTO updatedSkill = skillService.update(id, s);
        return ResponseEntity.ok(updatedSkill);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        skillService.delete(id);
        return ResponseEntity.ok("Skill Deleted");
    }

    //Find By Category id
    @PreAuthorize("permitAll()")
    @GetMapping("category/{id}")
    public List<SkillResponseDTO> getByCategoryId(@PathVariable Long id) {
        return skillService.getSkillByCategoryId(id);
    }

    //Find By Category Name
    @PreAuthorize("permitAll()")
    @GetMapping("category/name/{name}")
    public List<SkillResponseDTO> getByCategoryName(@PathVariable String name) {
        return skillService.getSkillByCategoryName(name);

    }

}
