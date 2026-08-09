package com.wordbridge.project.controller;

import com.wordbridge.project.dto.requestdto.CountryRequestDTO;
import com.wordbridge.project.dto.responsedto.CountryResponseDTO;
import com.wordbridge.project.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/countries/")
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CountryResponseDTO> save(@RequestBody CountryRequestDTO cr) {
        CountryResponseDTO savedCountry = countryService.save(cr);
        return ResponseEntity.ok(savedCountry);
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    public List<CountryResponseDTO> getAll() {
        return countryService.getAll();

    }

    @PreAuthorize("permitAll()")
    @GetMapping("{id}")
    public ResponseEntity<CountryResponseDTO> getById(@PathVariable Long id) {
        CountryResponseDTO country = countryService.findById(id);

        return ResponseEntity.ok(country);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        countryService.delete(id);
        return ResponseEntity.ok("Country Deleted");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<CountryResponseDTO> update(@RequestBody CountryRequestDTO c, @PathVariable Long id) {

        CountryResponseDTO updatedCountry = countryService.update(id, c);
        return ResponseEntity.ok(updatedCountry);
    }
}
