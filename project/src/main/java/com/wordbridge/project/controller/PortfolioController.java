package com.wordbridge.project.controller;

import com.wordbridge.project.dto.requestdto.PortfolioRequestDTO;
import com.wordbridge.project.dto.responsedto.PortfolioResponseDTO;
import com.wordbridge.project.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/portfolios/")
@RequiredArgsConstructor
public class PortfolioController {
    private final PortfolioService portfolioService;


    @PostMapping
    public ResponseEntity<PortfolioResponseDTO> save(
            @RequestPart("portfolio") PortfolioRequestDTO dto,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        return new ResponseEntity<>(
                portfolioService.save(dto, file),
                HttpStatus.CREATED
        );
    }


    @GetMapping
    public ResponseEntity<List<PortfolioResponseDTO>> getAll() {
        List<PortfolioResponseDTO> list = portfolioService.getAll();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("{id}")
    public ResponseEntity<PortfolioResponseDTO> getById(@PathVariable Long id) {
        PortfolioResponseDTO dto = portfolioService.findById(id);

        return ResponseEntity.ok(dto);
    }

    @PutMapping("{id}")
    public ResponseEntity<PortfolioResponseDTO> update(@RequestPart("portfolio") PortfolioRequestDTO dto,
                                                       @RequestPart(value = "file", required = false) MultipartFile file,
                                                       @PathVariable Long id) {

        PortfolioResponseDTO updated = portfolioService.update(id, dto, file);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        portfolioService.delete(id);
        return ResponseEntity.ok("Portfolio  Deleted");
    }

    @DeleteMapping("{id}/file")
    public ResponseEntity<String> deleteFile(@PathVariable Long id) {

        portfolioService.deleteFile(id);

        return ResponseEntity.ok(
                "Portfolio File successfully deleted"
        );
    }

    //Find Portfolio By User Profile id
    @GetMapping("userprofile/{userProfileId}")
    public ResponseEntity<List<PortfolioResponseDTO>> getByUserProfileId(
            @PathVariable Long userProfileId) {
        List<PortfolioResponseDTO> list = portfolioService.findByUserProfileId(userProfileId);

        return ResponseEntity.ok(list);
    }

    @GetMapping("count/userprofile/{userProfileId}")
    public ResponseEntity<Long> countByUserProfileId(
            @PathVariable Long userProfileId) {

        return ResponseEntity.ok(
                portfolioService.countByUserProfileId(userProfileId)
        );
    }


}
