package com.wordbridge.project.ai.service;

import com.wordbridge.project.ai.AIInterviewSessionResponseDTO;
import org.springframework.stereotype.Service;


public interface AIInterviewService {
    AIInterviewSessionResponseDTO startInterview(Long applicationId);

    AIInterviewSessionResponseDTO submitInterview(
            AIInterviewSessionResponseDTO dto);

AIInterviewSessionResponseDTO findByApplicationId(Long applicationId);





}
