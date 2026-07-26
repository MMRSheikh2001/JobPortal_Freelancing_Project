package com.wordbridge.project.controller;


import com.wordbridge.project.dto.requestdto.DivisionRequestDTO;
import com.wordbridge.project.dto.responsedto.DivisionResponseDTO;
import com.wordbridge.project.service.DivisionService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/divisions/")
@RequiredArgsConstructor
public class DivisionController {

    private final DivisionService divisionService;

    @PostMapping
    public ResponseEntity<DivisionResponseDTO> save(@RequestBody DivisionRequestDTO d) {
        DivisionResponseDTO savedDivision = divisionService.save(d);
        return ResponseEntity.ok(savedDivision);
    }

    @GetMapping
    public ResponseEntity<List<DivisionResponseDTO>> getAll() {
        List<DivisionResponseDTO> list = divisionService.getAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("{id}")
    public ResponseEntity<DivisionResponseDTO> getById(@PathVariable Long id) {
        DivisionResponseDTO dv = divisionService.getById(id);

        return ResponseEntity.ok(dv);
    }

    @PutMapping("{id}")
    public ResponseEntity<DivisionResponseDTO> update(@RequestBody DivisionRequestDTO d, @PathVariable Long id) {

        DivisionResponseDTO updatedDivision = divisionService.update(id, d);
        return ResponseEntity.ok(updatedDivision);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        divisionService.delete(id);
        return ResponseEntity.ok("Division Deleted");
    }

    //Find By Country id
    @GetMapping("country/{id}")
    public List<DivisionResponseDTO> getByCountryId(@PathVariable Long id) {
        return divisionService.getDivisionByCountryId(id);
    }

    //Find By Country Name
    @GetMapping("country/name/{name}")
    public List<DivisionResponseDTO> getByCountryName(@PathVariable String name) {
        return divisionService.getDivisionByCountryName(name);

    }
}
