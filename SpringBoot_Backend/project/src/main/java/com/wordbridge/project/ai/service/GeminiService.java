package com.wordbridge.project.ai.service;

import org.springframework.stereotype.Service;

@Service
public interface GeminiService {
    String askGemini(String prompt);
}
