package com.wordbridge.project.controller;

import com.wordbridge.project.dto.requestdto.PoliceStationRequestDTO;
import com.wordbridge.project.dto.responsedto.PoliceStationResponseDTO;
import com.wordbridge.project.service.PoliceStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policestations/")
@RequiredArgsConstructor
public class PoliceStationController {


    private final PoliceStationService policeStationService;

    @PostMapping
    public ResponseEntity<PoliceStationResponseDTO> save(@RequestBody PoliceStationRequestDTO ps) {
        PoliceStationResponseDTO savedPS = policeStationService.save(ps);
        return ResponseEntity.ok(savedPS);
    }

    @GetMapping
    public ResponseEntity<List<PoliceStationResponseDTO>> getAll() {
        List<PoliceStationResponseDTO> list = policeStationService.getAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("{id}")
    public ResponseEntity<PoliceStationResponseDTO> getById(@PathVariable Long id) {
        PoliceStationResponseDTO ps = policeStationService.getById(id);

        return ResponseEntity.ok(ps);
    }

    @PutMapping("{id}")
    public ResponseEntity<PoliceStationResponseDTO> update(@PathVariable Long id, @RequestBody PoliceStationRequestDTO ps) {

        PoliceStationResponseDTO updatedPS = policeStationService.update(id, ps);
        return ResponseEntity.ok(updatedPS);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        policeStationService.delete(id);
        return ResponseEntity.ok("Delete Police Station By Id" + id);
    }

    //Find By District Id
    @GetMapping("district/{id}")
    public List<PoliceStationResponseDTO> getByDistrictId(@PathVariable Long id) {
        return policeStationService.getPSByDistrictId(id);
    }

    //Find By District Name
    @GetMapping("district/name/{name}")
    public List<PoliceStationResponseDTO> getByDistrictName(@PathVariable String name) {
        return policeStationService.getPSByDistrictName(name);

    }

}
