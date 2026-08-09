package com.wordbridge.project.ai.service;

import com.wordbridge.project.ai.ResumeScreeningResult;
import org.springframework.stereotype.Service;

@Service
public interface ResumeScreeningService {

void screenApplication(Long applicationId);

    ResumeScreeningResult calculateJobMatch(
            Long jobId,
            Long userProfileId
    );

}
