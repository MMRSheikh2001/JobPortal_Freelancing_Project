package com.wordbridge.project.ai.service;

import org.springframework.stereotype.Service;

@Service
public interface ResumeScreeningService {

void screenApplication(Long applicationId);

}
