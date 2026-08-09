package com.wordbridge.project.ai;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AIInterviewSessionResponseDTO {

    

    private Long applicationId;

    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    private Integer totalScore;

    private Boolean completed;

    private List<InterviewQuestion> questions;

}
