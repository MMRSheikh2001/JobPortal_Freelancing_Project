package com.wordbridge.project.controller;


import com.wordbridge.project.dto.requestdto.AddressRequestDTO;
import com.wordbridge.project.dto.responsedto.AddressResponseDTO;


import com.wordbridge.project.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses/")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<AddressResponseDTO> save(@RequestBody AddressRequestDTO a) {
        AddressResponseDTO savedAddress = addressService.save(a);
        return ResponseEntity.ok(savedAddress);
    }

    @GetMapping
    public ResponseEntity<List<AddressResponseDTO>> getAll() {
        List<AddressResponseDTO> list = addressService.getAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("{id}")
    public ResponseEntity<AddressResponseDTO> getById(@PathVariable Long id) {
        AddressResponseDTO ds = addressService.findById(id);
        return ResponseEntity.ok(ds);
    }

    @PutMapping("{id}")
    public ResponseEntity<AddressResponseDTO> update(@RequestBody AddressRequestDTO ds, @PathVariable Long id) {

        AddressResponseDTO updatedAddress = addressService.update(id,ds);
        return ResponseEntity.ok(updatedAddress);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        addressService.delete(id);
        return ResponseEntity.ok("Address Deleted of Id  : " + id);
    }


    //Find By Country Id
    @GetMapping("country/{id}")
    public List<AddressResponseDTO> getByCountryId(@PathVariable Long id) {
        return addressService.findByCountryId(id);
    }

    //Find By Country Name
    @GetMapping("country/name/{name}")
    public List<AddressResponseDTO> getByCountryName(@PathVariable String name) {
        return addressService.findByCountryName(name);

    }

    //Find By Division Id
    @GetMapping("division/{id}")
    public List<AddressResponseDTO> getByDivisionId(@PathVariable Long id) {
        return addressService.findByDivisionId(id);
    }

    //Find By Division Name
    @GetMapping("division/name/{name}")
    public List<AddressResponseDTO> getByDivisionName(@PathVariable String name) {
        return addressService.findByDivisionName(name);

    }


    //Find By District Id
    @GetMapping("district/{id}")
    public List<AddressResponseDTO> getByDistrictId(@PathVariable Long id) {
        return addressService.findByDistrictId(id);
    }

    //Find By District Name
    @GetMapping("district/name/{name}")
    public List<AddressResponseDTO> getByDistrictName(@PathVariable String name) {
        return addressService.findByDistrictName(name);

    }

    //Find By Police Station Id
    @GetMapping("policestation/{id}")
    public List<AddressResponseDTO> getByPoliceStationId(@PathVariable Long id) {
        return addressService.findByPoliceStationId(id);
    }

    //Find By  Police Station Name
    @GetMapping("policestation/name/{name}")
    public List<AddressResponseDTO> getByPoliceStationName(@PathVariable String name) {
        return addressService.findByPoliceStationName(name);

    }

}
