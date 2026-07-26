package com.wordbridge.project.ai.serviceimpl;

import com.wordbridge.project.ai.dto.*;
import com.wordbridge.project.ai.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GeminiServiceImpl implements GeminiService {
    private final RestClient restClient;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String geminiUrl;


    @Override
    public String askGemini(String prompt) {

        //Converting to gemini request format
        Part part = new Part();
        part.setText(prompt);
        Content content = new Content();
        List<Part> parts = List.of(part);

        content.setParts(parts);

        GeminiRequest request = new GeminiRequest();
        List<Content> contents = List.of(content);
        request.setContents(contents);

        //Sending Request and getting response
        GeminiResponse response;
        try {
            // API call
            response = restClient.post()
                    .uri(geminiUrl + "?key=" + apiKey)
                    .body(request)
                    .retrieve()
                    .body(GeminiResponse.class);

        } catch (Exception e) {
            throw new RuntimeException("Gemini API Error: " + e.getMessage());
        }


        //Null Return handle
        if (response == null
                || response.getCandidates() == null
                || response.getCandidates().isEmpty()) {
            throw new RuntimeException("No response from Gemini");
        }
        Candidate candidate = response.getCandidates().get(0);

        if (candidate.getContent() == null
                || candidate.getContent().getParts() == null
                || candidate.getContent().getParts().isEmpty()) {
            throw new RuntimeException("Gemini returned empty content");
        }

        return candidate.getContent().getParts().get(0).getText();


    }
}
