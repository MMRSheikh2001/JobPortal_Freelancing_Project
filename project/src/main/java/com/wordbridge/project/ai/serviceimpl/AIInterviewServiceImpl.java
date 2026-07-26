package com.wordbridge.project.ai.serviceimpl;

import com.wordbridge.project.ai.*;
import com.wordbridge.project.ai.service.AIInterviewService;
import com.wordbridge.project.ai.service.GeminiService;
import com.wordbridge.project.dto.responsedto.ResumeResponseDTO;
import com.wordbridge.project.enums.ApplicationStatus;
import com.wordbridge.project.job.Job;
import com.wordbridge.project.job.JobMapper;
import com.wordbridge.project.job.JobResponseDTO;
import com.wordbridge.project.jobapplication.JobApplication;
import com.wordbridge.project.jobapplication.JobApplicationRepository;
import com.wordbridge.project.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AIInterviewServiceImpl implements AIInterviewService {
    private final AIInterviewQuestionRepository aiInterviewQuestionRepository;
    private final AIInterviewSessionRepository aiInterviewSessionRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final ResumeService resumeService;
    private final JobMapper jobMapper;
    private final ObjectMapper objectMapper;
    private final GeminiService geminiService;


    //User starts interview and gets ai generated questions

    @Override
    public AIInterviewSessionResponseDTO startInterview(Long applicationId) {


        JobApplication jobApplication = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("No Job Application found"));

        Job job = jobApplication.getJob();
        if (!Boolean.TRUE.equals(job.getAiScreeningEnabled())
                || !Boolean.TRUE.equals(job.getAiInterviewEnabled())) {
            throw new RuntimeException("AI Interview is disabled for this job.");
        }

        if (jobApplication.getAiInterviewCompleted()) {
            throw new RuntimeException("Interview already completed.");
        }


        Long userProfileId = jobApplication.getUserProfile().getId();
        ResumeResponseDTO resumeResponseDTO = resumeService.generateResume(userProfileId);

        //Building prompt
        String prompt = generateQuestionPrompt(job, resumeResponseDTO);

        //Giving prompt and receiving answer
        String geminiAnswer = geminiService.askGemini(prompt);

        //Converting string answer to new class for storing
        InterviewQuestionResult interviewQuestionResult;
        try {
            interviewQuestionResult = objectMapper.readValue(
                    geminiAnswer, InterviewQuestionResult.class
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (interviewQuestionResult.getQuestions().size() != job.getAiQuestionCount()) {
            throw new RuntimeException("Gemini returned an incorrect number of questions.");
        }

        AIInterviewSessionResponseDTO aiInterviewSessionResponseDTO = new AIInterviewSessionResponseDTO();


        aiInterviewSessionResponseDTO.setApplicationId(applicationId);
        aiInterviewSessionResponseDTO.setStartedAt(LocalDateTime.now());
        aiInterviewSessionResponseDTO.setCompleted(false);


        aiInterviewSessionResponseDTO.setQuestions(interviewQuestionResult.getQuestions());


        return aiInterviewSessionResponseDTO;
    }

    //User answers question and submits,the ai evaluates and we save the score and others in the database
    @Transactional
    @Override
    public AIInterviewSessionResponseDTO submitInterview(AIInterviewSessionResponseDTO dto) {
        JobApplication jobApplication = jobApplicationRepository.findById(dto.getApplicationId())
                .orElseThrow(() -> new RuntimeException("No Job Application found"));
        Job job = jobApplication.getJob();
        Long userProfileId = jobApplication.getUserProfile().getId();
        ResumeResponseDTO resumeResponseDTO = resumeService.generateResume(userProfileId);

        //Building prompt
        String prompt = generateEvaluationPrompt(job, resumeResponseDTO, dto);

        //Giving prompt and receiving answer
        String geminiAnswer = geminiService.askGemini(prompt);

        //Converting string answer to new class for storing
        InterviewQuestionResult interviewQuestionResult;
        try {
            interviewQuestionResult = objectMapper.readValue(
                    geminiAnswer, InterviewQuestionResult.class
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //Setting Everything to response dto to return to user for a clarity
        AIInterviewSessionResponseDTO aiInterviewSessionResponseDTO = new AIInterviewSessionResponseDTO();

        aiInterviewSessionResponseDTO.setApplicationId(dto.getApplicationId());
        aiInterviewSessionResponseDTO.setStartedAt(dto.getStartedAt());
        aiInterviewSessionResponseDTO.setCompletedAt(LocalDateTime.now());
        aiInterviewSessionResponseDTO.setTotalScore(interviewQuestionResult.getTotalScore());
        aiInterviewSessionResponseDTO.setCompleted(true);


        aiInterviewSessionResponseDTO.setQuestions(interviewQuestionResult.getQuestions());

        //Now lets save everything to database

        //Create AiInterviewSession and save
        AIInterviewSession session = new AIInterviewSession();

        session.setApplication(jobApplication);
        session.setStartedAt(aiInterviewSessionResponseDTO.getStartedAt());
        session.setCompletedAt(aiInterviewSessionResponseDTO.getCompletedAt());
        session.setTotalScore(aiInterviewSessionResponseDTO.getTotalScore());
        session.setInterviewCompleted(aiInterviewSessionResponseDTO.getCompleted());

        AIInterviewSession savedSession = aiInterviewSessionRepository.save(session);

        //Now create List of AIInterviewQuestion and save
        List<AIInterviewQuestion> entities = aiInterviewSessionResponseDTO.getQuestions()
                .stream()
                .map(q -> {
                    AIInterviewQuestion entity = new AIInterviewQuestion();

                    entity.setSession(savedSession);
                    entity.setQuestion(q.getQuestion());
                    entity.setAnswer(q.getAnswer());
                    entity.setScore(q.getScore());

                    return entity;
                })
                .collect(Collectors.toList());

        aiInterviewQuestionRepository.saveAll(entities);

        //Saving both side to be double sure or triple sure
        savedSession.setAiInterviewQuestions(entities);
        aiInterviewSessionRepository.save(savedSession);

        //After all that the JobApplication entity is also updated and saved
        jobApplication.setAiInterviewScore(aiInterviewSessionResponseDTO.getTotalScore());

        jobApplication.setAiInterviewCompleted(true);
        jobApplication.setAiCompletedAt(LocalDateTime.now());
        jobApplication.setStatus(ApplicationStatus.AI_COMPLETED);

        //getting ai match score from when user applied and it was saved in job application
        int resumeScore = jobApplication.getAiMatchScore();
        int interviewScore = savedSession.getTotalScore();

        int finalScore =
                (resumeScore * 40 + interviewScore * 60) / 100;

        jobApplication.setAiFinalScore(finalScore);

        if (finalScore >= job.getAiMatchThreshold()) {
            jobApplication.setStatus(ApplicationStatus.AUTOMATIC_QUALIFIED);

        } else {
            jobApplication.setStatus(ApplicationStatus.AI_COMPLETED);
        }


        jobApplicationRepository.save(jobApplication);


        return aiInterviewSessionResponseDTO;
    }

    @Override
    public AIInterviewSessionResponseDTO findByApplicationId(Long applicationId) {
        AIInterviewSession aiInterviewSession = aiInterviewSessionRepository.findByApplicationId(applicationId);
        AIInterviewSessionResponseDTO aiInterviewSessionResponseDTO = new AIInterviewSessionResponseDTO();

        aiInterviewSessionResponseDTO.setApplicationId(aiInterviewSession.getApplication().getId());
        if (aiInterviewSession.getStartedAt() != null) {
            aiInterviewSessionResponseDTO.setStartedAt(aiInterviewSession.getStartedAt());

        }
        if (aiInterviewSession.getCompletedAt() != null) {
            aiInterviewSessionResponseDTO.setCompletedAt(aiInterviewSession.getCompletedAt());
        }
        if (aiInterviewSession.getTotalScore() != null) {
            aiInterviewSessionResponseDTO.setTotalScore(aiInterviewSession.getTotalScore());
        }
        if (aiInterviewSession.getInterviewCompleted() == null) {
            aiInterviewSessionResponseDTO.setCompleted(false);
        } else {
            aiInterviewSessionResponseDTO.setCompleted(aiInterviewSession.getInterviewCompleted());
        }

        List<AIInterviewQuestion> questions = aiInterviewQuestionRepository.findBySessionId(aiInterviewSession.getId());

        List<InterviewQuestion> interviewQuestions = questions.stream()
                .map(question -> {
                    InterviewQuestion dto = new InterviewQuestion();

                    dto.setQuestion(question.getQuestion());
                    dto.setAnswer(question.getAnswer());
                    dto.setScore(question.getScore());
                    return dto;
                })
                .toList();
        aiInterviewSessionResponseDTO.setQuestions(interviewQuestions);


        return aiInterviewSessionResponseDTO;
    }


    //Generate Question Prompt Building
    private String generateQuestionPrompt(Job job, ResumeResponseDTO resume) {

        JobResponseDTO jobResponseDTO = jobMapper.toDTO(job);
        int count = job.getAiQuestionCount();

        String jobJson;
        String resumeJson;

        try {
            jobJson = objectMapper.writeValueAsString(jobResponseDTO);
            resumeJson = objectMapper.writeValueAsString(resume);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return """
                You are an experienced technical HR interviewer.
                
                Your task is to generate interview questions by comparing the Job description and the Candidate Resume.
                
                Instructions:
                
                1. Read the Job information carefully.
                2. Read the Candidate Resume carefully.
                3. Generate EXACTLY """ + count + """
                interview questions.
                4. Every question must be unique.
                5. Questions must be directly relevant to the job requirements.
                6. Use the candidate's resume whenever possible.
                7. Include a balanced mix of:
                   - Technical questions
                   - Problem-solving questions
                   - Project-related questions
                   - Behavioral questions
                8. If the resume lacks information, generate questions from the job requirements.
                9. Do NOT generate duplicate questions.
                10. Do NOT include answers.
                11. Do NOT number the questions.
                12. Do NOT include explanations.
                13. Do NOT include markdown.
                14. Do NOT wrap the response inside ```json``` blocks.
                15. Return ONLY valid JSON.
                16. Do not output any text before or after the JSON.
                
                Return EXACTLY this JSON structure:
                
                {
                  "questions": [
                    {
                      "question": "..."
                    }
                  ]
                }
                
                The "questions" array MUST contain EXACTLY """ + count + """
                question objects.
                
                Job:
                """
                + jobJson
                + """
                
                Candidate Resume:
                """
                + resumeJson;
    }

    //Evaluate Answers of JobSeeker Using gemini
    private String generateEvaluationPrompt(
            Job job, ResumeResponseDTO resume, AIInterviewSessionResponseDTO dto
    ) {

        JobResponseDTO jobResponseDTO = jobMapper.toDTO(job);

        String jobJson;
        String resumeJson;
        String interviewJson;
        try {
            jobJson = objectMapper.writeValueAsString(jobResponseDTO);
            resumeJson = objectMapper.writeValueAsString(resume);
            interviewJson = objectMapper.writeValueAsString(dto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String prompt = """
                You are an experienced senior technical interviewer and HR evaluator.
                
                Your task is to evaluate the candidate's interview performance.
                
                You will receive:
                
                1. The Job details.
                2. The Candidate Resume.
                3. The Interview Questions.
                4. The Candidate's Answers.
                
                Evaluation Rules:
                
                1. Evaluate every answer independently.
                2. Score each answer from 0 to 10.
                3. Give higher scores for:
                
                   * Technical correctness
                   * Clear explanation
                   * Relevance to the question
                   * Practical knowledge
                   * Problem-solving ability
                4. Give lower scores for:
                
                   * Incorrect answers
                   * Missing answers
                   * Off-topic answers
                   * Very short or vague answers
                5. Do NOT change any question.
                6. Do NOT change any answer.
                7. Preserve the original order of the questions.
                8. Calculate the totalScore as the sum of all individual scores.
                9. Do NOT add any explanations or comments.
                10. Do NOT include markdown.
                11. Do NOT wrap the response inside code fences.
                12. Your response must be a single valid JSON object and nothing else.
                13. Do not output any text before or after the JSON.
                14. Preserve the exact question text.
                15. Preserve the exact answer text.
                16. Do not rewrite, correct grammar, or shorten any answer.
                17.If an answer is empty or missing, assign a score of 0.
                18.Every question must have a score.
                19.When scoring, consider whether the answer is consistent with the candidate's stated experience and projects in the resume.
                
                
                Return ONLY this JSON format:
                
                {
                "totalScore": 85,
                "questions": [
                {
                "question": "...",
                "answer": "...",
                "score": 8
                }
                ]
                }
                
                The "questions" array MUST contain exactly the same number of questions that were provided.
                
                Job:                
                
                """ + jobJson +
                "                           Candidate Resume:     " + resumeJson
                + "                                Interview:      " + interviewJson;


        return prompt;
    }

}


