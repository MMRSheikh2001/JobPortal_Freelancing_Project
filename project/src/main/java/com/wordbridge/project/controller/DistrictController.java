package com.wordbridge.project.controller;

import com.wordbridge.project.dto.requestdto.DistrictRequestDTO;
import com.wordbridge.project.dto.responsedto.DistrictResponseDTO;
import com.wordbridge.project.service.DistrictService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/districts/")
@RequiredArgsConstructor
public class DistrictController {

    private final DistrictService districtService;

    @PostMapping
    public ResponseEntity<DistrictResponseDTO> save(@RequestBody DistrictRequestDTO ds) {
        DistrictResponseDTO savedDistrict = districtService.save(ds);
        return ResponseEntity.ok(savedDistrict);
    }

    @GetMapping
    public ResponseEntity<List<DistrictResponseDTO>> getAll() {
        List<DistrictResponseDTO> list = districtService.getAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("{id}")
    public ResponseEntity<DistrictResponseDTO> getById(@PathVariable Long id) {
        DistrictResponseDTO ds = districtService.findById(id);

        return ResponseEntity.ok(ds);
    }

    @PutMapping("{id}")
    public ResponseEntity<DistrictResponseDTO> update(@RequestBody DistrictRequestDTO ds, @PathVariable Long id) {

        DistrictResponseDTO updatedDistrict = districtService.update(id, ds);
        return ResponseEntity.ok(updatedDistrict);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        districtService.delete(id);
        return ResponseEntity.ok("District Deleted of Id" + id);
    }


    //Find By Division Id
    @GetMapping("division/{id}")
    public List<DistrictResponseDTO> getByDivisionId(@PathVariable Long id) {
        return districtService.getDistrictByDivisionId(id);
    }

    //Find By Division Name
    @GetMapping("division/name/{name}")
    public List<DistrictResponseDTO> getByDivisionName(@PathVariable String name) {
        return districtService.getDistrictByDivisionName(name);

    }
}
