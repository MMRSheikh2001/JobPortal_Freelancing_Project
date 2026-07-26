package com.wordbridge.project.controller;

import com.wordbridge.project.dto.requestdto.CompanyProfileRequestDTO;
import com.wordbridge.project.dto.responsedto.CompanyProfileResponseDTO;
import com.wordbridge.project.job.CompanySearchRequestDTO;
import com.wordbridge.project.service.CompanyProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/companies/")
@RequiredArgsConstructor
public class CompanyProfileController {

    private final CompanyProfileService companyProfileService;


    @PostMapping
    public ResponseEntity<CompanyProfileResponseDTO> save(
            @RequestPart("companyprofile") CompanyProfileRequestDTO cp,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return new ResponseEntity<>(
                companyProfileService.save(cp, image),
                HttpStatus.CREATED
        );
    }


    @GetMapping
    public ResponseEntity<List<CompanyProfileResponseDTO>> getAll() {
        List<CompanyProfileResponseDTO> list = companyProfileService.getAll();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("{id}")
    public ResponseEntity<CompanyProfileResponseDTO> getById(@PathVariable Long id) {
        CompanyProfileResponseDTO cp = companyProfileService.findById(id);

        return ResponseEntity.ok(cp);
    }

    @PutMapping("{id}")
    public ResponseEntity<CompanyProfileResponseDTO> update(@RequestPart("companyprofile") CompanyProfileRequestDTO cp,
                                                            @RequestPart(value = "image", required = false) MultipartFile image,
                                                            @PathVariable Long id) {

        CompanyProfileResponseDTO updated = companyProfileService.update(id, cp, image);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        companyProfileService.delete(id);
        return ResponseEntity.ok("Company Profile Deleted");
    }

    @DeleteMapping("{id}/image")
    public ResponseEntity<String> deleteImage(@PathVariable Long id) {

        companyProfileService.deleteImage(id);

        return ResponseEntity.ok(
                "Profile image deleted successfully"
        );
    }

    //Find User Profile By User Id
    @GetMapping("user/{userId}")
    public ResponseEntity<CompanyProfileResponseDTO> getByUserId(
            @PathVariable Long userId) {
        CompanyProfileResponseDTO cp = companyProfileService.findByUserId(userId);
        return ResponseEntity.ok(cp);
    }

    //Find By address


    @GetMapping("location/policestation/{id}")
    public List<CompanyProfileResponseDTO> findByLocationPoliceStationId(@PathVariable Long id) {
        return companyProfileService.findByLocationPoliceStationId(id);
    }

    @GetMapping("location/policestation/district/{id}")
    public List<CompanyProfileResponseDTO> findByLocationPoliceStationDistrictId(@PathVariable Long id) {
        return companyProfileService.findByLocationPoliceStationDistrictId(id);
    }

    @GetMapping("exists/user/{userId}")
    public ResponseEntity<Boolean> existsByUserId(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                companyProfileService.existsByUserId(userId)
        );
    }

    @GetMapping("count")
    public ResponseEntity<Long> count() {

        return ResponseEntity.ok(
                companyProfileService.count()
        );
    }

    //Find by Keyword
    @GetMapping("search")
    public List<CompanyProfileResponseDTO> search(
            @RequestParam String keyword) {

        return companyProfileService.search(keyword);
    }

    @PostMapping("filter")
    public ResponseEntity<List<CompanyProfileResponseDTO>> search(
            @RequestBody CompanySearchRequestDTO request
    ){
        System.out.println("===== FILTER ENDPOINT HIT =====");

        return ResponseEntity.ok(
                companyProfileService.search(request)
        );

    }

}
