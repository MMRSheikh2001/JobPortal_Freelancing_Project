package com.wordbridge.project.ai.serviceimpl;

import com.wordbridge.project.ai.ResumeScreeningResult;
import com.wordbridge.project.ai.service.GeminiService;
import com.wordbridge.project.ai.service.ResumeScreeningService;
import com.wordbridge.project.dto.responsedto.ResumeResponseDTO;
import com.wordbridge.project.entity.UserProfile;
import com.wordbridge.project.enums.ApplicationStatus;
import com.wordbridge.project.job.Job;
import com.wordbridge.project.job.JobMapper;
import com.wordbridge.project.job.JobRepository;
import com.wordbridge.project.job.JobResponseDTO;
import com.wordbridge.project.jobapplication.JobApplication;
import com.wordbridge.project.jobapplication.JobApplicationRepository;
import com.wordbridge.project.repository.UserProfileRepository;
import com.wordbridge.project.service.ResumeService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ResumeScreeningServiceImpl implements ResumeScreeningService {
    private final JobApplicationRepository jobApplicationRepository;
    private final ResumeService resumeService;
    private final JobMapper jobMapper;
    private final GeminiService geminiService;
    private final ObjectMapper objectMapper;

    private final JobRepository jobRepository;


    @Override
    public void screenApplication(Long applicationId) {
        JobApplication jobApplication = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("No Job Application found"));

        Job job = jobApplication.getJob();
        if (!Boolean.TRUE.equals(job.getAiScreeningEnabled())
                || !Boolean.TRUE.equals(job.getAiCvScreeningEnabled())) {
            return;
        }

        UserProfile userProfile = jobApplication.getUserProfile();

        ResumeResponseDTO resumeResponseDTO = resumeService.generateResume(userProfile.getId());


        //building prompt
        String prompt = buildPrompt(job, resumeResponseDTO);

        //Giving prompt and receiving answer
        String geminiAnswer = geminiService.askGemini(prompt);

        //  Converting string answer to new class for storing
        ResumeScreeningResult resumeScreeningResult;
        try {
            resumeScreeningResult = objectMapper.readValue(
                    geminiAnswer, ResumeScreeningResult.class
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        jobApplication.setAiMatchScore(resumeScreeningResult.getMatchScore());
        jobApplication.setAiMatchFeedback(resumeScreeningResult.getFeedback());
        if (job.getAiInterviewEnabled()
                && (jobApplication.getStatus() == ApplicationStatus.APPLIED
                || jobApplication.getStatus() == ApplicationStatus.AI_PENDING)) {

            if (resumeScreeningResult.getMatchScore() >= job.getAiMatchThreshold()) {

                jobApplication.setStatus(ApplicationStatus.AI_PENDING);
                jobApplication.setAiDeadlineDate(
                        LocalDateTime.now().plusDays(job.getAiDeadlineDays())
                );

            } else {

                jobApplication.setStatus(ApplicationStatus.APPLIED);
                jobApplication.setAiDeadlineDate(null);

            }

        }


        jobApplicationRepository.save(jobApplication);


    }

    @Override
    public ResumeScreeningResult calculateJobMatch(
            Long jobId,
            Long userProfileId
    ) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new RuntimeException("Job not found"));


        ResumeResponseDTO resumeResponseDTO =
                resumeService.generateResume(userProfileId);

        String prompt =
                buildPrompt(job, resumeResponseDTO);

        String geminiAnswer =
                geminiService.askGemini(prompt);

        try {

            return objectMapper.readValue(
                    geminiAnswer,
                    ResumeScreeningResult.class
            );

        } catch (Exception e) {

            throw new RuntimeException(e);

        }
    }


    private String buildPrompt(Job job,
                               ResumeResponseDTO resume) {

        JobResponseDTO jobResponseDTO = jobMapper.toDTO(job);

        String jobJson;
        String resumeJson;


        try {
            jobJson = objectMapper.writeValueAsString(jobResponseDTO);
            resumeJson = objectMapper.writeValueAsString(resume);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String prompt =
                """
                        You are an expert HR recruiter.
                        
                        Compare the following job and resume.
                        
                        Return ONLY valid JSON.
                        
                        Do NOT use markdown.
                        Do NOT wrap the JSON in ```json.
                        Do NOT explain your answer.
                        
                        The response must exactly match:
                        
                        {
                          "matchScore": 0,
                          "feedback": ""
                        }
                        
                        {
                          "matchScore": 0-100,
                          "feedback": "..."
                        }
                        
                        Job:
                        """ +
                        jobJson +
                        """
                                
                                Resume:
                                """ +
                        resumeJson;


        return prompt;
    }


}


