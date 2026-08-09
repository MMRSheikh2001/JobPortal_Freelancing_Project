package com.wordbridge.project.controller;

import com.wordbridge.project.dto.requestdto.DistrictRequestDTO;
import com.wordbridge.project.dto.responsedto.DistrictResponseDTO;
import com.wordbridge.project.service.DistrictService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/districts/")
@RequiredArgsConstructor
public class DistrictController {

    private final DistrictService districtService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<DistrictResponseDTO> save(@RequestBody DistrictRequestDTO ds) {
        DistrictResponseDTO savedDistrict = districtService.save(ds);
        return ResponseEntity.ok(savedDistrict);
    }


    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<List<DistrictResponseDTO>> getAll() {
        List<DistrictResponseDTO> list = districtService.getAll();
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("{id}")
    public ResponseEntity<DistrictResponseDTO> getById(@PathVariable Long id) {
        DistrictResponseDTO ds = districtService.findById(id);

        return ResponseEntity.ok(ds);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<DistrictResponseDTO> update(@RequestBody DistrictRequestDTO ds, @PathVariable Long id) {

        DistrictResponseDTO updatedDistrict = districtService.update(id, ds);
        return ResponseEntity.ok(updatedDistrict);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        districtService.delete(id);
        return ResponseEntity.ok("District Deleted of Id" + id);
    }


    //Find By Division Id
    @PreAuthorize("permitAll()")
    @GetMapping("division/{id}")
    public List<DistrictResponseDTO> getByDivisionId(@PathVariable Long id) {
        return districtService.getDistrictByDivisionId(id);
    }

    //Find By Division Name
    @PreAuthorize("permitAll()")
    @GetMapping("division/name/{name}")
    public List<DistrictResponseDTO> getByDivisionName(@PathVariable String name) {
        return districtService.getDistrictByDivisionName(name);

    }
}
