package com.wordbridge.project.ai.controller;

import com.wordbridge.project.ai.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class GeminiController {
    private final GeminiService geminiService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/test")
    public String test(@RequestParam String prompt) {
        return geminiService.askGemini(prompt);
    }


}
