package com.wordbridge.project.controller;

import com.wordbridge.project.dto.requestdto.TrainingRequestDTO;
import com.wordbridge.project.dto.responsedto.TrainingResponseDTO;
import com.wordbridge.project.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/trainings/")
@RequiredArgsConstructor
public class TrainingController {
    private final TrainingService trainingService;


    @PostMapping
    public ResponseEntity<TrainingResponseDTO> save(
            @RequestPart("training") TrainingRequestDTO dto,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        return new ResponseEntity<>(
                trainingService.save(dto, file),
                HttpStatus.CREATED
        );
    }


    @GetMapping
    public ResponseEntity<List<TrainingResponseDTO>> getAll() {
        List<TrainingResponseDTO> list = trainingService.getAll();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("{id}")
    public ResponseEntity<TrainingResponseDTO> getById(@PathVariable Long id) {
        TrainingResponseDTO dto = trainingService.findById(id);

        return ResponseEntity.ok(dto);
    }

    @PutMapping("{id}")
    public ResponseEntity<TrainingResponseDTO> update(@RequestPart("training") TrainingRequestDTO dto,
                                                      @RequestPart(value = "file", required = false) MultipartFile file,
                                                      @PathVariable Long id) {

        TrainingResponseDTO updated = trainingService.update(id, dto, file);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        trainingService.delete(id);
        return ResponseEntity.ok("Training Deleted");
    }

    @DeleteMapping("{id}/file")
    public ResponseEntity<String> deleteFile(@PathVariable Long id) {

        trainingService.deleteFile(id);

        return ResponseEntity.ok(
                "Training File successfully"
        );
    }

    //Find Trainings By User Profile id
    @GetMapping("userprofile/{userProfileId}")
    public ResponseEntity<List<TrainingResponseDTO>> getByUserProfileId(
            @PathVariable Long userProfileId) {
        List<TrainingResponseDTO> list = trainingService.findByUserProfileId(userProfileId);

        return ResponseEntity.ok(list);
    }

    @GetMapping("count/userprofile/{userProfileId}")
    public ResponseEntity<Long> countByUserProfileId(
            @PathVariable Long userProfileId) {

        return ResponseEntity.ok(
                trainingService.countByUserProfileId(userProfileId)
        );
    }


}
