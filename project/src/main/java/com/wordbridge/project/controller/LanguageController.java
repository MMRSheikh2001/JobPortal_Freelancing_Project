package com.wordbridge.project.controller;

import com.wordbridge.project.dto.requestdto.LanguageRequestDTO;
import com.wordbridge.project.dto.responsedto.LanguageResponseDTO;
import com.wordbridge.project.service.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/languages/")
@RequiredArgsConstructor
public class LanguageController {

    private final LanguageService languageService;


    @PostMapping
    public ResponseEntity<LanguageResponseDTO> save(@RequestBody LanguageRequestDTO l) {
        LanguageResponseDTO savedLanguage = languageService.save(l);
        return ResponseEntity.ok(savedLanguage);
    }

    @GetMapping
    public List<LanguageResponseDTO> getAll() {
        return languageService.getAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<LanguageResponseDTO> getById(@PathVariable Long id) {
        LanguageResponseDTO language = languageService.findById(id);

        return ResponseEntity.ok(language);

    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        languageService.delete(id);
        return ResponseEntity.ok("Language Deleted");
    }

    @PutMapping("{id}")
    public ResponseEntity<LanguageResponseDTO> update(@RequestBody LanguageRequestDTO l, @PathVariable Long id) {

        LanguageResponseDTO updatedLanguage = languageService.update(id, l);
        return ResponseEntity.ok(updatedLanguage);
    }


}
