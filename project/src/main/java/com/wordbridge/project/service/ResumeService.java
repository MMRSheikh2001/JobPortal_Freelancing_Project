package com.wordbridge.project.service;

import com.wordbridge.project.dto.responsedto.ResumeResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface ResumeService {
    ResumeResponseDTO generateResume(
            Long userProfileId
    );

    String generateHtml(Long userProfileId);


    byte[] generatePdf(
            Long userProfileId
    );

}
