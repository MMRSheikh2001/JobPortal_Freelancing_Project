package com.wordbridge.project.controller;


import com.wordbridge.project.dto.requestdto.SkillRequestDTO;
import com.wordbridge.project.dto.responsedto.SkillResponseDTO;
import com.wordbridge.project.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills/")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;


    @PostMapping
    public ResponseEntity<SkillResponseDTO> save(@RequestBody SkillRequestDTO s) {
        SkillResponseDTO savedSkill = skillService.save(s);
        return ResponseEntity.ok(savedSkill);
    }

    @GetMapping
    public ResponseEntity<List<SkillResponseDTO>> getAll() {
        List<SkillResponseDTO> list = skillService.getAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("{id}")
    public ResponseEntity<SkillResponseDTO> getById(@PathVariable Long id) {
        SkillResponseDTO skill = skillService.findById(id);
        return ResponseEntity.ok(skill);
    }

    @PutMapping("{id}")
    public ResponseEntity<SkillResponseDTO> update(@RequestBody SkillRequestDTO s, @PathVariable Long id) {

        SkillResponseDTO updatedSkill = skillService.update(id, s);
        return ResponseEntity.ok(updatedSkill);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        skillService.delete(id);
        return ResponseEntity.ok("Skill Deleted");
    }

    //Find By Category id
    @GetMapping("category/{id}")
    public List<SkillResponseDTO> getByCategoryId(@PathVariable Long id) {
        return skillService.getSkillByCategoryId(id);
    }

    //Find By Category Name
    @GetMapping("category/name/{name}")
    public List<SkillResponseDTO> getByCategoryName(@PathVariable String name) {
        return skillService.getSkillByCategoryName(name);

    }

}
