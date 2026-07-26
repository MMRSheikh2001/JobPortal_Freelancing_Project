package com.wordbridge.project.ai.controller;

import com.wordbridge.project.ai.AIInterviewSessionResponseDTO;
import com.wordbridge.project.ai.service.AIInterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai/interview/")
@RequiredArgsConstructor
public class AIInterviewController {
    private final AIInterviewService aiInterviewService;

    @PostMapping("start/{applicationId}")
    public AIInterviewSessionResponseDTO startInterview(
            @PathVariable Long applicationId) {

        return aiInterviewService.startInterview(applicationId);
    }


    @PostMapping("submit")
    public AIInterviewSessionResponseDTO submitInterview(
            @RequestBody AIInterviewSessionResponseDTO dto) {

        return aiInterviewService.submitInterview(dto);
    }

    @GetMapping("{applicationId}")
    public AIInterviewSessionResponseDTO getByApplicationId(@PathVariable Long applicationId) {
        return aiInterviewService.findByApplicationId(applicationId);
    }

}
