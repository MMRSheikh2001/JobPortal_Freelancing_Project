package com.wordbridge.project.controller;


import com.wordbridge.project.dto.requestdto.UserSkillRequestDTO;
import com.wordbridge.project.dto.responsedto.UserSkillResponseDTO;
import com.wordbridge.project.service.UserSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/userskills/")
@RequiredArgsConstructor
public class UserSkillController {

    private final UserSkillService userSkillService;


    @PostMapping
    public ResponseEntity<UserSkillResponseDTO> save(@RequestBody UserSkillRequestDTO us) {
        UserSkillResponseDTO savedUserSkill = userSkillService.save(us);
        return ResponseEntity.ok(savedUserSkill);
    }

    @GetMapping
    public ResponseEntity<List<UserSkillResponseDTO>> getAll() {
        List<UserSkillResponseDTO> list = userSkillService.getAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserSkillResponseDTO> getById(@PathVariable Long id) {
        UserSkillResponseDTO us = userSkillService.findById(id);
        return ResponseEntity.ok(us);
    }

    @PutMapping("{id}")
    public ResponseEntity<UserSkillResponseDTO> update(@RequestBody UserSkillRequestDTO us, @PathVariable Long id) {

        UserSkillResponseDTO updatedUserSkill = userSkillService.update(id, us);
        return ResponseEntity.ok(updatedUserSkill);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        userSkillService.delete(id);
        return ResponseEntity.ok("User Skill Deleted");
    }


    @GetMapping("userprofile/{id}")
    public List<UserSkillResponseDTO> findByUserProfileId(@PathVariable Long id) {
        return userSkillService.findByUserProfileId(id);
    }

    @GetMapping("skill/{id}")
    public List<UserSkillResponseDTO> findBySkillId(@PathVariable Long id) {
        return userSkillService.findBySkillId(id);
    }

    @GetMapping("skill/category/{id}")
    public List<UserSkillResponseDTO> findBySkillCategoryId(@PathVariable Long id) {
        return userSkillService.findBySkillCategoryId(id);
    }

    @GetMapping("userprofile/{userProfileId}/skill/{skillId}")
    public ResponseEntity<UserSkillResponseDTO> findByUserProfileIdAndSkillId(
            @PathVariable Long userProfileId,
            @PathVariable Long skillId) {

        UserSkillResponseDTO dto = userSkillService
                .findByUserProfileIdAndSkillId(userProfileId, skillId);


        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("userprofile/{userProfileId}/skill/{skillId}")
    public ResponseEntity<String> deleteByUserProfileIdAndSkillId(@PathVariable Long userProfileId, @PathVariable Long skillId) {

        userSkillService.deleteByUserProfileIdAndSkillId(userProfileId, skillId);
        return ResponseEntity.ok("User Skill Deleted");
    }

    @GetMapping("userprofile/count/{userProfileId}")
    public ResponseEntity<Long> countSkillsByUserProfileId(@PathVariable Long userProfileId) {
        return ResponseEntity.ok(userSkillService.countSkillsByUserProfileId(userProfileId));
    }

}
