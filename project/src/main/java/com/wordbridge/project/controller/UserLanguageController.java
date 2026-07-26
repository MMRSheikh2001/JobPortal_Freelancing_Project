package com.wordbridge.project.controller;

import com.wordbridge.project.dto.requestdto.UserLanguageRequestDTO;
import com.wordbridge.project.dto.responsedto.UserLanguageResponseDTO;
import com.wordbridge.project.service.UserLanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/userlanguages/")
@RequiredArgsConstructor
public class UserLanguageController {

    private final UserLanguageService userLanguageService;


    @PostMapping
    public ResponseEntity<UserLanguageResponseDTO> save(@RequestBody UserLanguageRequestDTO ul) {
        UserLanguageResponseDTO saved = userLanguageService.save(ul);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<UserLanguageResponseDTO>> getAll() {
        List<UserLanguageResponseDTO> list = userLanguageService.getAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserLanguageResponseDTO> getById(@PathVariable Long id) {
        UserLanguageResponseDTO ul = userLanguageService.findById(id);
        return ResponseEntity.ok(ul);
    }

    @PutMapping("{id}")
    public ResponseEntity<UserLanguageResponseDTO> update(@RequestBody UserLanguageRequestDTO ul, @PathVariable Long id) {

        UserLanguageResponseDTO updated = userLanguageService.update(id, ul);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        userLanguageService.delete(id);
        return ResponseEntity.ok("User Language Deleted");
    }


    @GetMapping("userprofile/{id}")
    public List<UserLanguageResponseDTO> findByUserProfileId(@PathVariable Long id) {
        return userLanguageService.findByUserProfileId(id);
    }

    @GetMapping("language/{id}")
    public List<UserLanguageResponseDTO> findByLanguageId(@PathVariable Long id) {
        return userLanguageService.findByLanguageId(id);
    }


    @GetMapping("userprofile/{userProfileId}/language/{languageId}")
    public ResponseEntity<UserLanguageResponseDTO> findByUserProfileIdAndLanguageId(
            @PathVariable Long userProfileId,
            @PathVariable Long languageId) {

        UserLanguageResponseDTO dto = userLanguageService
                .findByUserProfileIdAndLanguageId(userProfileId, languageId);


        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("userprofile/{userProfileId}/language/{languageId}")
    public ResponseEntity<String> deleteByUserProfileIdAndLanguageId(@PathVariable Long userProfileId, @PathVariable Long languageId) {

        userLanguageService.deleteByUserProfileIdAndLanguageId(userProfileId, languageId);
        return ResponseEntity.ok("User Language Deleted");
    }

    @GetMapping("userprofile/count/{userProfileId}")
    public ResponseEntity<Long> countLanguagesByUserProfileId(@PathVariable Long userProfileId) {
        return ResponseEntity.ok(userLanguageService.countLanguagesByUserProfileId(userProfileId));
    }


}
